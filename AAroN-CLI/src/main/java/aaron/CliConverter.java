package aaron;

import aaron.export.AAroNCsvWriter;
import aaron.logging.Logger;
import aaron.model.Model;
import aaron.sparx.*;
import aaron.sparx.config.Config;
import aaron.sparx.config.DBToImport;
import aaron.sparx.config.DBType;
import aaron.sparx.config.MSSQLDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.apache.commons.io.comparator.PathFileComparator;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CommandLine.Command(name = "convert", mixinStandardHelpOptions = true)
public class CliConverter implements Callable<Integer> {

    private static final Pattern sparxFilePattern = Pattern.compile(".*\\.(eapx|eap|qea|qeax|feap)");
    private final Logger logger = new CliLogger();

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @CommandLine.ArgGroup(exclusive = true, multiplicity = "0..1", validate = true)
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
                arity = "1..*", required = false, paramLabel = "FILE",
                description = "The file to be converted and imported into a graph db.")
        File[] files;

        @CommandLine.Option(
                names = {"-d", "--directory"},
                arity = "1", required = false, paramLabel = "DIRECTORY",
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
                            "value is not a directory.", file));
        }
        this.outputDir = file;
    }

    @Override
    public Integer call() throws IOException {
        logger.info("Start AAroN CLI converter");
        Config config = loadConfigOrDefault();
        Path outputPath = outputDir.toPath();
        File outputFile = outputPath.resolve("aaron_output.yml").toFile();
        try(FileOutputStream fos = new FileOutputStream(outputFile)) {
            List<String> filesToConvert = config.getFilesToConvert();
            if (importArg != null && importArg.directory != null) {
                List<File> filesInFolder = determineFilesToImport(config, importArg.directory);
                filesInFolder.forEach(file -> {
                    logger.info("Adding file in folder to conversion list: " + file.getAbsolutePath());
                    filesToConvert.add(file.getAbsolutePath());
                });
            }
            if (importArg != null && importArg.files != null) {
                Arrays.stream(importArg.files).forEach(file -> {
                    logger.info("Adding file to conversion list: " + file.getAbsolutePath());
                    filesToConvert.add(file.getAbsolutePath());
                });
            }
            convert(config, fos);
        } catch (AAroNConversionException e) {
            logger.error("Conversion error: " + e.getMessage(), e);
            return 1;
        }
        logger.info("End AAroN CLI converter");
        return 0;
    }

    void convert(Config config, OutputStream outputStream) throws AAroNConversionException, IOException {
        AAronCLIOutput output = new AAronCLIOutput();
        Path outputPath = outputDir.toPath();
        List<ConversionJob> conversionJobs = new ArrayList<>();

        //Files
        List<String> filesToConvert = config.getFilesToConvert();
        for (String eapFileName : filesToConvert) {
            Path path = Path.of(eapFileName);
            File eapFile;
            if (path.isAbsolute()) {
                eapFile = path.toFile();
            } else {
                eapFile = outputPath.resolve(path).toFile();
            }
            logger.info("Create conversion job for file: " + eapFile);
            conversionJobs.add(createConversionJob(config, outputPath, eapFile));
        }

        //Databases
        List<DBToImport> dbsToImport = config.getDbsToImport();
        for (DBToImport dbToImport : dbsToImport) {
            if (StringUtils.isNotBlank(dbToImport.getDatabase())) {
                logger.info("Create conversion job for DB: " + dbToImport.getDatabase());
                conversionJobs.add(createConversionJob(config, outputPath, dbToImport));
            } else {
                logger.warn("Can not create conversion job for DB: " + dbToImport.getDatabase());
            }
        }

        processConversionJobs(conversionJobs, output);

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.SPLIT_LINES));
        ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(outputStream, output);
    }

    private Optional<Path> findConfigInDirectory(File directory) throws IOException {
        try (Stream<Path> stream = Files.walk(directory.toPath())) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(file -> "aaron_config.yml".equals(file.getFileName().toString()))
                    .findFirst();
        }
    }

    private Config loadConfigOrDefault() throws IOException {
        Config config;
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
                logger.info("Filename: " + fileName);
                File file = new File(fileName);
                if (file.isAbsolute() && file.exists()) {
                    logger.info("isAbsolute: " + file.isAbsolute());
                    fileList.add(file);
                } else {
                    if (directory != null) {
                        Path resolved = directory.toPath().resolve(fileName);
                        File resolvedFile = resolved.toFile();
                        if (resolvedFile.exists()) {
                            fileList.add(resolvedFile);
                        }
                        else {
                            logger.warn("Can't resolve file. Ignore and proceed: " + fileName);
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

    private AbstractSparxConverter getSparxConverter(Config config, File eapFile) {
        AbstractSparxConverter converter = null;
        var eapFileName = eapFile.getName();
        String lowerCaseName = eapFileName.toLowerCase();
        if (lowerCaseName.endsWith(".eap") || lowerCaseName.endsWith(".eapx")) {
            converter = new SparxJETConverter(config, eapFile, logger);
        }
        if (lowerCaseName.endsWith(".qea") || lowerCaseName.endsWith(".qeax")) {
            converter = new SparxSQLiteConverter(config, eapFile, logger);
        }
        if (lowerCaseName.endsWith(".feap")) {
            converter = new SparxFirebirdConverter(config, eapFile, logger);
        }
        return converter;
    }

    private ConversionJob createConversionJob(Config config, Path outputPath, File eapFile) {
        ConversionJob job = new ConversionJob();
        String eapFileName = eapFile.getName();
        job.converter = getSparxConverter(config, eapFile);
        job.nodesFile = outputPath.resolve("nodes_" + eapFileName + ".csv").toFile();
        job.edgesFile = outputPath.resolve("edges_" + eapFileName + ".csv").toFile();
        return job;
    }

    private ConversionJob createConversionJob(Config config, Path outputPath, DBToImport dbToImport) {
        ConversionJob job = new ConversionJob();
        DBType type = dbToImport.getType();
        switch (type) {
            case MSSQL:
                job.converter = new SparxMSSQLConverter(config, (MSSQLDB) dbToImport, logger);
                break;
            case MySQL:
                job.converter = new SparxMySQLConverter(config, dbToImport.getHostname(), dbToImport.getPort(), dbToImport.getDatabase(), dbToImport.getUsername(), dbToImport.getPassword(), logger);
                break;
        }
        job.nodesFile = outputPath.resolve("nodes_" + dbToImport.getHostname()+ "_" + dbToImport.getDatabase() + ".csv").toFile();
        job.edgesFile = outputPath.resolve("edges_" + dbToImport.getHostname()+ "_" + dbToImport.getDatabase() + ".csv").toFile();
        return job;
    }

    private static void processConversionJobs(List<ConversionJob> conversionJobs, AAronCLIOutput output) throws AAroNConversionException {
        for (ConversionJob conversionJob : conversionJobs) {
            AbstractSparxConverter converter = conversionJob.converter;
            File nodesFile = conversionJob.nodesFile;
            File edgesFile = conversionJob.edgesFile;
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
    }

    static class ConversionJob {
        AbstractSparxConverter converter;
        File nodesFile;
        File edgesFile;
    }

}
