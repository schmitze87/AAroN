package aaron;

import aaron.model.Model;
import aaron.sparx.Config;
import aaron.sparx.SparxJETConverter;
import aaron.export.AAroNCsvWriter;
import org.neo4j.cli.ExecutionContext;
import org.neo4j.configuration.connectors.BoltConnector;
import org.neo4j.configuration.connectors.HttpConnector;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.importer.ImportCommand;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "aaron", footer = "Copyright(c) 2021 Markus Schmitz", mixinStandardHelpOptions = true, version = "2021.06", subcommands = {AAroNCLI.Import.class, AAroNCLI.Create.class})
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

    private static void registerShutdownHook( final DatabaseManagementService managementService )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                stop = true;
                managementService.shutdown();
            }
        } );
    }

    @CommandLine.Command(name = "create", mixinStandardHelpOptions = true, version = "2022.12")
    static class Create implements Callable<Integer> {

        @CommandLine.Spec
        CommandLine.Model.CommandSpec spec;

        @CommandLine.ArgGroup(exclusive = true, multiplicity = "1")
        Import.FileImport fileImport;

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
                try {
                    File eapFile = fileImport.singleFile;
                    Path neo4jHome = this.outputDir.toPath().normalize();
                    Path neo4jConf = neo4jHome.resolve("conf");
                    Path importPath = neo4jHome.resolve("import");
                    Files.createDirectories(neo4jConf);
                    Files.createDirectories(importPath);
                    File nodesFile = importPath.resolve("nodes.csv").toFile();
                    File edgesFile = importPath.resolve("edges.csv").toFile();
                    Map<String, Object> config = new HashMap<>();
                    config.put("taggedValues", "AS_PROPERTY");
                    SparxJETConverter converter = new SparxJETConverter(Config.createFromMap(config), eapFile);
                    Model model = converter.convert();
                    AAroNCsvWriter.write(model, nodesFile, edgesFile);

                    ExecutionContext executionContext = new ExecutionContext(neo4jHome, neo4jConf);
                    CommandLine importCmd = new CommandLine(new ImportCommand(executionContext));
                    int importExecResult = importCmd.execute(new String[]{"--database=neo4j", "--delimiter=,", "--array-delimiter=;", "--quote=\"", "--legacy-style-quoting=true", "--multiline-fields=true", "--ignore-extra-columns=true" ,"--nodes="+nodesFile.getAbsolutePath(), "--relationships="+edgesFile.getAbsolutePath()});
                    if (importExecResult == 0) {
                        DatabaseManagementService databaseManagementService = new DatabaseManagementServiceBuilder(neo4jHome)
                                .setConfig( BoltConnector.enabled, true )
                                .setConfig( HttpConnector.enabled, true )
                                .build();
                        registerShutdownHook(databaseManagementService);
                        databaseManagementService.listDatabases().stream().forEach(s -> System.out.println(s));
                        GraphDatabaseService neo4j = databaseManagementService.database("neo4j");
                        if (neo4j.isAvailable(10000)) {
                            Runtime.getRuntime().exec("powershell Start-Process \"http://browser.graphapp.io/\"");
                            while (!AAroNCLI.stop) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                }
                            }
                            return 0;
                        } else {
                            databaseManagementService.shutdown();
                            return 1;
                        }
                    } else {
                        return 1;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return 1;
                }
            }
            if (fileImport.multiFile != null) {

                return 0;
            }
            return 1;
        }
    }

    @CommandLine.Command(name = "import", mixinStandardHelpOptions = true, version = "2022.12")
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
                SparxJETConverter converter = new SparxJETConverter(Config.createFromMap(config), eapFile);
                try {
                    Model model = converter.convert();
                    AAroNCsvWriter.write(model, nodesFile, edgesFile);
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
