package aaron;

import aaron.export.AAroNCsvWriter;
import aaron.model.Model;
import aaron.sparx.AbstractSparxConverter;
import aaron.sparx.Config;
import aaron.sparx.SparxJETConverter;
import aaron.sparx.SparxSQLiteConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.comparator.PathFileComparator;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CommandLine.Command(name = "aaron", footer = "Copyright(c) 2024 Markus Schmitz", mixinStandardHelpOptions = true, version = "2024.06", subcommands = {
        AAroNCLI.Convert.class
})
public class AAroNCLI implements Callable<Integer> {

    private static final Pattern sparxFilePattern = Pattern.compile(".*\\.(eapx|eap|qea|qeax)");

    @Override
    public Integer call() {
        System.out.println("Try to use the import command");
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new AAroNCLI()).execute(args);
        System.exit(exitCode);
    }

    @CommandLine.Command(name = "convert", mixinStandardHelpOptions = true, version = "2024.01")
    static class Convert implements Callable<Integer> {

        @CommandLine.Spec
        CommandLine.Model.CommandSpec spec;

        @CommandLine.ArgGroup(exclusive = true, multiplicity = "1")
        Import importArg;

        @CommandLine.Option(
                names = {"-c", "--config"},
                arity = "0..1", required = false, paramLabel = "FILE",
                description = "The directory you want to import from.")
        File configFile = null;

        File outputDir;

        static class Import {
            @CommandLine.Option(
                    names = {"-f", "--file"},
                    arity = "1..*", required = true, paramLabel = "FILE",
                    description = "The file to be converted and imported into a graph db.")
            File[] files;

            @CommandLine.Option(
                    names = {"-d", "--directory"},
                    arity = "1", required = true, paramLabel = "DIRECTORY",
                    description = "The directory you want to import from.")
            File directory;
        }

        @CommandLine.Option(
                names = {"-o", "--outputDir"},
                required = true, paramLabel = "OUTPUT_DIR",
                description = "usually this is the import directory of your neo4j dbms.")
        public void setOutputDir(File file) {
            if (!file.isDirectory()) {
                throw new CommandLine.ParameterException(spec.commandLine(),
                        String.format("Invalid value '%s' for option '--outputDir': " +
                                "value is not a directory.", file.toString()));
            }
            this.outputDir = file;
        }

        private Optional<Path> findConfigInDirectory(File directory) throws IOException {
            try (Stream<Path> stream = Files.walk(importArg.directory.toPath())) {
                return stream
                        .filter(Files::isRegularFile)
                        .filter(file -> "aaron_config.yml".equals(file.getFileName().toString()))
                        .findFirst();
            }
        }

        private Config loadConfigOrDefault() throws IOException {
            Config config = null;
            if (configFile != null) {
                config = Config.loadFromYAML(configFile);
                return config;
            }
            if (importArg != null && importArg.directory != null) {
                Optional<Path> configInDirectory = findConfigInDirectory(importArg.directory);
                if (configInDirectory.isPresent()) {
                    config = Config.loadFromYAML(configInDirectory.get().toFile());
                    return config;
                }
            }
            // Default
            Map<String, Object> configMap = new HashMap<>();
            configMap.put("taggedValues", "AS_PROPERTY");
            config = Config.createFromMap(configMap);
            return config;
        }

        private List<File> determineFilesToImport(Config config, File directory) throws IOException {
            if (!config.getFilesToConvert().isEmpty()) {
                // Online import specified files
                List<File> fileList = new ArrayList<>();
                config.getFilesToConvert().forEach(fileName -> {
                    System.out.println("Filename: " + fileName);
                    File file = new File(fileName);
                    if (file.isAbsolute() && file.exists()) {
                        System.out.println("isAbsolute: " + file.isAbsolute());
                        fileList.add(file);
                    } else {
                        if (directory != null) {
                            Path resolved = directory.toPath().resolve(fileName);
                            File resolvedFile = resolved.toFile();
                            if (resolvedFile.exists()) {
                                fileList.add(resolvedFile);
                            }
                            else {
                                System.out.println("Can't resolve file. Ignore and proceed: " + fileName);
                            }
                        }
                    }
                });
                return fileList;
            } else {
                // import all files from directory
                return findSparxFilesInDirectory(importArg.directory);
            }
        }

        private List<File> findSparxFilesInDirectory(File directory) throws IOException {
            try (Stream<Path> stream = Files.walk(directory.toPath())) {
                return stream
                        .filter(Files::isRegularFile)
                        .filter(file -> sparxFilePattern.matcher(file.getFileName().toString()).matches())
                        .map(Path::toFile)
                        .sorted(PathFileComparator.PATH_SYSTEM_COMPARATOR)
                        .collect(Collectors.toList());
            }
        }

        private void convert(List<File> fileList, Config config) throws AAroNConversionException, IOException {
            AAronCLIOutput output = new AAronCLIOutput();
            Path outputPath = outputDir.toPath();
            for (File eapFile : fileList) {
                var eapFileName = eapFile.getName();
                File nodesFile = outputPath.resolve("nodes_" + eapFileName + ".csv").toFile();
                File edgesFile = outputPath.resolve("edges_" + eapFileName + ".csv").toFile();
                AbstractSparxConverter converter = null;
                String lowerCaseName = eapFileName.toLowerCase();
                if (lowerCaseName.endsWith("eap") || lowerCaseName.endsWith("eapx")) {
                    converter = new SparxJETConverter(config, eapFile);
                }
                if (lowerCaseName.endsWith("qea") || lowerCaseName.endsWith("qeax")) {
                    converter = new SparxSQLiteConverter(config, eapFile);
                }
                try {
                    if (converter != null) {
                        Model model = converter.convert();
                        AAroNCsvWriter.write(model, nodesFile, edgesFile);
                    } else {
                        throw new AAroNConversionException();
                    }
                    output.getNodesToImport().add(nodesFile.getAbsoluteFile().toString());
                    output.getEdgesToImport().add(edgesFile.getAbsoluteFile().toString());
                } catch (IOException e) {
                    throw new AAroNConversionException(e);
                }
            }
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
            File outputFile = outputPath.resolve("aaron_output.yml").toFile();
            objectWriter.writeValue(outputFile, output);

        }

        @Override
        public Integer call() throws IOException {
            Config config = loadConfigOrDefault();
            try {
                if (importArg != null && importArg.directory != null) {
                    List<File> fileList = determineFilesToImport(config, importArg.directory);
                    convert(fileList, config);
                }
                if (importArg != null && importArg.files != null) {
                    convert(Arrays.asList(importArg.files), config);
                }
            } catch (AAroNConversionException e) {
                e.printStackTrace();
                return 1;
            }
            return 0;
        }
    }
}
