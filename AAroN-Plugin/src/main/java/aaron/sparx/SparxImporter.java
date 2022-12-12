package aaron.sparx;

import aaron.model.Converter;
import aaron.model.Model;
import aaron.model.ModelProcessor;
import aaron.util.ProgressInfo;
import aaron.util.ProgressReporter;
import aaron.util.Util;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class SparxImporter {

    @Context
    public GraphDatabaseService db;

    @Context
    public Log log;

    public SparxImporter() {

    }

    @Procedure(name = "aaron.import.sparxeap", mode = Mode.WRITE)
    @Description("Imports an Sparx Enterprise Architect Project (EAP) File")
    public Stream<ProgressInfo> importEAP(@Name("eapFileName") String fileName, @Name(value = "config", defaultValue = "{}") Map<String, Object> configMap) throws InterruptedException {
        Config config = Config.createFromMap(configMap);
        String importFolder;
        try (Transaction transaction = db.beginTx()) {
            importFolder = Util.getImportFolder(transaction);
        }
        File file = new File(importFolder, fileName).getAbsoluteFile();
        CompletableFuture<ProgressInfo> future = CompletableFuture.supplyAsync(() -> {
            ProgressInfo progressInfo = new ProgressInfo(fileName, "file", "eap");
            progressInfo.batchSize = 1000;
            final ProgressReporter reporter = new ProgressReporter(null, new PrintWriter(System.out), progressInfo);
            String upperCaseFileName = file.getName().toUpperCase();
            int i = upperCaseFileName.lastIndexOf('.');
            if (i == -1) {
                log.error("Can not determine EA-Repository type. Missing file extension");
                throw new RuntimeException("Can not determine EA-Repository type. Missing file extension");
            }
            String extension = upperCaseFileName.substring(i + 1);
            Converter converter = null;
            switch (extension) {
                case "EAP":
                case "EAPX":
                    converter = new SparxJETConverter(config, file);
                    break;
                case "QEA":
                    converter = new SparxSQLiteConverter(config, file);
                    break;
            }
            Model model = null;
            if (converter == null) {
                log.error("Can not determine EA-Repository type. Unknown file extension");
                throw new RuntimeException("Can not determine EA-Repository type. Unknown file extension");
            }
            try {
                model = converter.convert();
            } catch (IOException e) {
                log.error("IO Error", e);
            }
            ModelProcessor modelProcessor = new ModelProcessor(db, reporter);
            modelProcessor.process(model);
            return reporter.getTotal();
        });
        try {
            ProgressInfo info = future.get();
            return Stream.of(info);
        } catch (InterruptedException e) {
            log.warn("Interrupted", e);
            throw e;
        } catch (ExecutionException e) {
            log.error("Could not process the results", e);
        }
        return Stream.empty();
    }

    @Procedure(name = "aaron.import.sparxMySQL", mode = Mode.WRITE)
    @Description("Imports an Sparx Enterprise Architect MySQL Project")
    public Stream<ProgressInfo> importMySQL(@Name("host") String host,
                                            @Name("port") long port,
                                            @Name("databaseName") String databaseName,
                                            @Name("username") String username,
                                            @Name("password") String password,
                                            @Name(value = "config", defaultValue = "{}") Map<String, Object> configMap) {
        Config config = Config.createFromMap(configMap);
        CompletableFuture<ProgressInfo> future = CompletableFuture.supplyAsync(() -> {
            ProgressInfo progressInfo = new ProgressInfo(databaseName, host, "MySQL");
            progressInfo.batchSize = 1000;
            final ProgressReporter reporter = new ProgressReporter(null, new PrintWriter(System.out), progressInfo);
            Converter converter = new SparxMySQLConverter(config, host, port, databaseName, username, password);
            Model model = null;
            try {
                model = converter.convert();
            } catch (IOException e) {
                log.error("IO Error", e);
            }
            ModelProcessor modelProcessor = new ModelProcessor(db, reporter);
            modelProcessor.process(model);
            return reporter.getTotal();
        });
        try {
            ProgressInfo info = future.get();
            return Stream.of(info);
        } catch (InterruptedException e) {
            log.warn("Interrupted", e);
        } catch (ExecutionException e) {
            log.error("Could not process the results", e);
        }
        return Stream.empty();
    }

}
