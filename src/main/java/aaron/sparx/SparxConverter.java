package aaron.sparx;

import aaron.Converter;
import aaron.neo4j.model.Model;
import aaron.sparx.model.*;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

public class SparxConverter implements Converter {

    private Model model;

    public Model convert(final File file) throws IOException {
        Database db = fixCharEncoding(DatabaseBuilder.open(file));
        model = new Model();
        processPackages(db.getTable(EAPackage.TABLE_NAME));
        processDiagrams(db.getTable(EADiagram.TABLE_NAME));
        processObjects(db.getTable(EAObject.TABLE_NAME));
        processConnectors(db.getTable(EAConnector.TABLE_NAME));
        processXRefs(db.getTable(EAXref.TABLE_NAME));
        return model;
    }

    private Database fixCharEncoding(final Database db) throws IOException {
        Database.FileFormat fileFormat = db.getFileFormat();
        if (fileFormat == Database.FileFormat.V1997) {
            db.setCharset(Charset.forName("cp1252"));
        }
        return db;
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processPackages(final T table) {
        Processor processor = new PackageProcessor(model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processObjects(final T table) {
        Processor processor = new ObjectProcessor(model);
        for (U row : table) {
            processor.process(row);
        }
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processConnectors(final T table) {
        Processor connectorProcessor = new ConnectorProcessor(model);
        for (U row : table) {
            connectorProcessor.process(row);
        }
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processDiagrams(final T table) {
        Processor processor = new DiagramProcessor(model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processXRefs(final T table) {
        Processor processor = new XRefProcessor(model);
        table.forEach(processor::process);
    }
}
