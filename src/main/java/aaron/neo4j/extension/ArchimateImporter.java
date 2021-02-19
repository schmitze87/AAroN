package aaron.neo4j.extension;

import aaron.apoc.result.ProgressInfo;
import aaron.archimate.ArchiMateConverter;
import aaron.neo4j.model.Model;
import aaron.apoc.export.util.ProgressReporter;
import org.neo4j.graphdb.*;
import org.neo4j.procedure.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class ArchimateImporter {

    @Context
    public GraphDatabaseService db;

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
            ArchiMateConverter converter = new ArchiMateConverter();
            Model model = converter.convert(file);
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
