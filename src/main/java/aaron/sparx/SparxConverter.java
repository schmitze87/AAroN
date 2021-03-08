package aaron.sparx;

import aaron.Converter;
import aaron.model.Model;
import aaron.sparx.model.*;
import aaron.sparx.processors.*;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Map;

public class SparxConverter implements Converter {

    private Model model;
    private Config config;

    public SparxConverter(final Config config) {
        this.config = config;
    }

    public Model convert(final File file) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String sha1 = null;
        try {
             sha1 = createSHA1(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Database db = fixCharEncoding(DatabaseBuilder.open(file));
        model = new Model();
        processPackages(sha1, now, db.getTable(EAPackage.TABLE_NAME));

        processDiagrams(sha1, now, db.getTable(EADiagram.TABLE_NAME));

        processObjects(sha1, now, db.getTable(EAObject.TABLE_NAME));
        processObjectProperties(sha1, now, db.getTable(EAObjectProperty.TABLE_NAME));
        processObjectConstraints(sha1, now, db.getTable(EAObjectConstraint.TABLE_NAME));

        processConnectors(sha1, now, db.getTable(EAConnector.TABLE_NAME));
        processConnectorTags(sha1, now, db.getTable(EAConnectorTag.TABLE_NAME));
        processConnectorConstraints(sha1, now, db.getTable(EAConnectorConstraint.TABLE_NAME));

        processOperations(sha1, now, db.getTable(EAOperation.TABLE_NAME));
        processOperationTags(sha1, now, db.getTable(EAOperationTag.TABLE_NAME));
        processOperationParams(sha1, now, db.getTable(EAOperationParams.TABLE_NAME));

        processAttributes(sha1, now, db.getTable(EAAttribute.TABLE_NAME));
        processAttributeTags(sha1, now, db.getTable(EAAttributeTag.TABLE_NAME));
        processAttributeConstraints(sha1, now, db.getTable(EAAttributeConstraint.TABLE_NAME));

        processXRefs(sha1, now, db.getTable(EAXref.TABLE_NAME));
        return model;
    }

    private Database fixCharEncoding(final Database db) throws IOException {
        Database.FileFormat fileFormat = db.getFileFormat();
        if (fileFormat == Database.FileFormat.V1997) {
            db.setCharset(Charset.forName("cp1252"));
        }
        return db;
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processObjectConstraints(
            final String sha1, final LocalDateTime time, final T table) {
        ObjectConstraintProcessor processor = new ObjectConstraintProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processAttributeConstraints(
            final String sha1, final LocalDateTime time, final T table) {
        AttributeConstraintProcessor processor = new AttributeConstraintProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processConnectorConstraints(
            final String sha1, final LocalDateTime time, final T table) {
        ConnectorConstraintProcessor processor = new ConnectorConstraintProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processPackages(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new PackageProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processObjects(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new ObjectProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processConnectors(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new ConnectorProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processConnectorTags(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new ConnectorTagProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processDiagrams(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new DiagramProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processXRefs(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new XRefProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processObjectProperties(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new ObjectPropertiesProcessor(sha1, time, model, config.getTaggedValueMode());
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processOperations(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new OperationProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processOperationTags(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new OperationTagProcessor(sha1, time, model, config.getTaggedValueMode());
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processOperationParams(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new OperationParamsProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processAttributes(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new AttributeProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> void processAttributeTags(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new AttributeTagProcessor(sha1, time, model, config.getTaggedValueMode());
        table.forEach(processor::process);
    }

    private <T extends Iterable<U>, U extends Map<String, Object>> T getTable(Database db, EATable table) throws IOException {
        return (T) db.getTable(table.getTableName());
    }

    private String createSHA1(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        InputStream fis = new FileInputStream(file);
        int n = 0;
        byte[] buffer = new byte[8192];
        while (n != -1) {
            n = fis.read(buffer);
            if (n > 0) {
                digest.update(buffer, 0, n);
            }
        }
        return new HexBinaryAdapter().marshal(digest.digest());
    }
}
