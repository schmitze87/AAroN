package aaron.sparx;

import aaron.Converter;
import aaron.model.Model;
import aaron.sparx.model.*;
import aaron.sparx.processors.*;
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
        processObjectProperties(db.getTable(EAObjectProperty.TABLE_NAME));

        processConnectors(db.getTable(EAConnector.TABLE_NAME));
        processConnectorTags(db.getTable(EAConnectorTag.TABLE_NAME));

        processOperations(db.getTable(EAOperation.TABLE_NAME));
        processOperationTags(db.getTable(EAOperationTag.TABLE_NAME));

        processAttributes(db.getTable(EAAttribute.TABLE_NAME));
        processAttributeTags(db.getTable(EAAttributeTag.TABLE_NAME));

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
        Processor processor = new ConnectorProcessor(model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processConnectorTags(final T table) {
        Processor processor = new ConnectorTagProcessor(model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processDiagrams(final T table) {
        Processor processor = new DiagramProcessor(model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processXRefs(final T table) {
        Processor processor = new XRefProcessor(model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processObjectProperties(final T table) {
        Processor processor = new ObjectPropertiesProcessor(model, TaggedValueMode.ADD_AS_NODE);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processOperations(final T table) {
        Processor processor = new OperationProcessor(model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processOperationTags(final T table) {
        Processor processor = new OperationTagProcessor(model, TaggedValueMode.ADD_AS_NODE);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processAttributes(final T table) {
        Processor processor = new AttributeProcessor(model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processAttributeTags(final T table) {
        Processor processor = new AttributeTagProcessor(model, TaggedValueMode.ADD_AS_NODE);
        table.forEach(processor::process);
    }
}
