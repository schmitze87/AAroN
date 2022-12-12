package aaron.archimate;

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
import java.io.PrintWriter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class ArchimateImporter {

    @Context
    public GraphDatabaseService db;

    @Context
    public Log log;

    public ArchimateImporter() {

    }

    // CALL atea.import.archiexchange("sample1.xml");
    @Procedure(name = "aaron.import.archiexchange", mode = Mode.WRITE)
    @Description("Imports an ArchiMate Open Exchange Format XML File")
    public Stream<ProgressInfo> importArchiExchangeXML(
            @Name("file") String fileName) {
        String importFolder;
        try (Transaction transaction = db.beginTx()) {
            importFolder = Util.getImportFolder(transaction);
        }
        File file = new File(importFolder, fileName).getAbsoluteFile();
        CompletableFuture<ProgressInfo> future = CompletableFuture.supplyAsync(() -> {
            ProgressInfo progressInfo = new ProgressInfo(fileName, "file", "xml");
            progressInfo.batchSize = 500;
            final ProgressReporter reporter = new ProgressReporter(null, new PrintWriter(System.out), progressInfo);
            ArchiMateConverter converter = new ArchiMateConverter(file);
            Model model = converter.convert();
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
        return Stream.of(null);
    }
}
