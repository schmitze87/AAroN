package aaron;

import aaron.model.Model;
import aaron.sparx.Config;
import aaron.sparx.SparxConverter;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "aaron", footer = "Copyright(c) 2021 Markus Schmitz", mixinStandardHelpOptions = true, version = "2021.06", subcommands = {AAroNCLI.Import.class})
public class AAroNCLI implements Callable<Integer> {

    @Override
    public Integer call() {
        System.out.println("Try to use the import command");
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new AAroNCLI()).execute(args);
        System.exit(exitCode);
    }

    @CommandLine.Command(name = "import", mixinStandardHelpOptions = true, version = "2021.06")
    static class Import implements Callable<Integer> {

        @CommandLine.Spec
        CommandLine.Model.CommandSpec spec;

        @CommandLine.ArgGroup(exclusive = true, multiplicity = "1")
        FileImport fileImport;

        File outputDir;

        static class FileImport {
            @CommandLine.Option(names = {"-f", "--file"}, required = true, paramLabel = "FILE", description = "The file to be converted and imported into a graph db.")
            File singleFile;

            @CommandLine.Option(names = {"-i", "--inputDir"}, required = true, paramLabel = "INPUT_DIR", description = "The directory from which all compatible files are to be converted and imported into a graph db.")
            File multiFile;
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
            if (fileImport.singleFile != null) {
                File eapFile = fileImport.singleFile;
                File outputDir = this.outputDir;
                File nodesFile = new File(outputDir, "nodes.csv");
                File edgesFile = new File(outputDir, "edges.csv");
                Map<String, Object> config = new HashMap<>();
                config.put("taggedValues", "AS_PROPERTY");
                SparxConverter converter = new SparxConverter(Config.createFromMap(config));
                try {
                    Model model = converter.convert(eapFile);
                    CSVWriter.write(model, nodesFile, edgesFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    return 1;
                }
                return 0;
            }
            if (fileImport.multiFile != null) {

                return 0;
            }
            return 1;
        }
    }
}
