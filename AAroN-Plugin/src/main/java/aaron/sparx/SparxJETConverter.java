package aaron.sparx;

import aaron.model.Model;
import aaron.sparx.model.*;
import aaron.util.Util;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Map;

public class SparxJETConverter extends AbstractSparxConverter {

    private final File file;

    public SparxJETConverter(final Config config, final File file) {
        super(new Model(), config);
        this.file = file;
    }

    public Model convert() throws IOException {
        LocalDateTime now = LocalDateTime.now();

        String sha1 = null;
        try {
             sha1 = Util.createSHA256(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Database db = fixCharEncoding(DatabaseBuilder.open(file));
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

    private <T extends Iterable<U>, U extends Map<String, Object>> T getTable(Database db, EATable table) throws IOException {
        return (T) db.getTable(table.getTableName());
    }

}
