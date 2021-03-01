package aaron.sparx;

import aaron.apoc.export.util.ProgressReporter;
import aaron.apoc.result.ProgressInfo;
import aaron.Converter;
import aaron.model.ModelProcessor;
import aaron.Util;
import aaron.model.Model;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class SparxImporter {

    @Context
    public GraphDatabaseService db;

    public SparxImporter() {

    }

    @Procedure(name = "aaron.import.sparxeap", mode = Mode.WRITE)
    @Description("Imports an Sparx Enterprise Architect Project (EAP) File")
    public Stream<ProgressInfo> importEAP(@Name("eapFileName") String fileName) {
        String importFolder;
        try (Transaction transaction = db.beginTx()) {
            importFolder = Util.getImportFolder(transaction);
        }
        File file = new File(importFolder, fileName).getAbsoluteFile();
        CompletableFuture<ProgressInfo> future = CompletableFuture.supplyAsync(() -> {
            ProgressInfo progressInfo = new ProgressInfo(fileName, "file", "eap");
            progressInfo.batchSize = 1000;
            final ProgressReporter reporter = new ProgressReporter(null, new PrintWriter(System.out), progressInfo);
            Converter converter = new SparxConverter();
            Model model = null;
            try {
                model = converter.convert(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ModelProcessor modelProcessor = new ModelProcessor(db, reporter);
            modelProcessor.process(model);
            return reporter.getTotal();
        });
        try {
            ProgressInfo info = future.get();
            return Stream.of(info);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Stream.of(null);
    }
}
