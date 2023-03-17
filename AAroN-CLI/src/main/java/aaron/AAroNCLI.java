package aaron;

import aaron.model.Model;
import aaron.sparx.AbstractSparxConverter;
import aaron.sparx.Config;
import aaron.sparx.SparxJETConverter;
import aaron.export.AAroNCsvWriter;
import aaron.sparx.SparxSQLiteConverter;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "aaron", footer = "Copyright(c) 2023 Markus Schmitz", mixinStandardHelpOptions = true, version = "2021.06", subcommands = {
        AAroNCLI.Convert.class
})
public class AAroNCLI implements Callable<Integer> {

    private static boolean stop = false;

    @Override
    public Integer call() {
        System.out.println("Try to use the import command");
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new AAroNCLI()).execute(args);
        System.exit(exitCode);
    }

    @CommandLine.Command(name = "convert", mixinStandardHelpOptions = true, version = "2022.12")
    static class Convert implements Callable<Integer> {

        @CommandLine.Spec
        CommandLine.Model.CommandSpec spec;

        @CommandLine.ArgGroup(exclusive = true, multiplicity = "1")
        FileImport fileImport;

        File outputDir;

        static class FileImport {
            @CommandLine.Option(names = {"-f", "--file"}, arity = "1..*", required = true, paramLabel = "FILE", description = "The file to be converted and imported into a graph db.")
            File[] files;
        }

        @CommandLine.Option(names = {"-o", "--outputDir"}, required = true, paramLabel = "OUTPUT_DIR", description = "usually this is the import directory of your neo4j dbms.")
        public void setOutputDir(File file) {
            if (!file.isDirectory()) {
                throw new CommandLine.ParameterException(spec.commandLine(),
                        String.format("Invalid value '%s' for option '--outputDir': " +
                                "value is not a directory.", file.toString()));
            }
            this.outputDir = file;
        }

        @Override
        public Integer call() {
            if (fileImport.files != null) {
                for (File eapFile : fileImport.files) {
                    var eapFileName = eapFile.getName();
                    Path outputPath = outputDir.toPath();
                    File nodesFile = outputPath.resolve("nodes_" + eapFileName + ".csv").toFile();
                    File edgesFile = outputPath.resolve("edges_" + eapFileName + ".csv").toFile();
                    Map<String, Object> config = new HashMap<>();
                    config.put("taggedValues", "AS_PROPERTY");
                    AbstractSparxConverter converter = null;
                    String lowerCaseName = eapFileName.toLowerCase();
                    if (lowerCaseName.endsWith("eap") || lowerCaseName.endsWith("eapx")) {
                        converter = new SparxJETConverter(Config.createFromMap(config), eapFile);
                    }
                    if (lowerCaseName.endsWith("qea") || lowerCaseName.endsWith("qeax")) {
                        converter = new SparxSQLiteConverter(Config.createFromMap(config), eapFile);
                    }
                    try {
                        if (converter != null) {
                            Model model = converter.convert();
                            AAroNCsvWriter.write(model, nodesFile, edgesFile);
                        } else {
                            return 1;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return 1;
                    }
                }
                return 0;
            }
            return 1;
        }
    }
}
