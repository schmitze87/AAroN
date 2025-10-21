package aaron.sparx;

import aaron.logging.Logger;
import aaron.model.Model;
import aaron.sparx.config.Config;
import aaron.sparx.model.*;
import aaron.util.Util;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Table;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.*;

public class SparxJETConverter extends AbstractSparxConverter {

    private final File file;

    public SparxJETConverter(final Config config, final File file, Logger logger) {
        super(new Model(), config, logger);
        this.file = file;
    }

    private void iterateJETTable(final String sha1, final LocalDateTime time, ProcessInterface processInterface, Table table) {
        table.forEach(row -> {
            List<Map<String, Object>> list = new ArrayList<>(1);
            Map<String, Object> rowMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String key = entry.getKey().toLowerCase(Locale.ROOT);
                Object value = entry.getValue();
                rowMap.put(key, value);
            }
            list.add(rowMap);
            processInterface.process(sha1, time, list);
        });
        System.gc();
    }

    public Model convert() throws IOException {
        LocalDateTime now = LocalDateTime.now();

        String sha1 = null;
        try {
            sha1 = Util.createSHA256(file);
            context.setFileHash(sha1);
        } catch (Exception e) {
            logger.error("Could not compute SHA-256 hash of " + file.getAbsolutePath(), e);
        }

        Database db = fixCharEncoding(DatabaseBuilder.open(file));

        iterateJETTable(sha1, now, this::processSystem, db.getTable(EASystem.TABLE_NAME));

        iterateJETTable(sha1, now, this::processSystem, db.getTable(EASystem.TABLE_NAME));

        iterateJETTable(sha1, now, this::processPackages, db.getTable(EAPackage.TABLE_NAME));

        iterateJETTable(sha1, now, this::processDiagrams, db.getTable(EADiagram.TABLE_NAME));

        iterateJETTable(sha1, now, this::processObjects, db.getTable(EAObject.TABLE_NAME));
        iterateJETTable(sha1, now, this::processObjectProperties, db.getTable(EAObjectProperty.TABLE_NAME));
        iterateJETTable(sha1, now, this::processObjectConstraints, db.getTable(EAObjectConstraint.TABLE_NAME));

        iterateJETTable(sha1, now, this::processConnectors, db.getTable(EAConnector.TABLE_NAME));
        iterateJETTable(sha1, now, this::processConnectorTags, db.getTable(EAConnectorTag.TABLE_NAME));
        iterateJETTable(sha1, now, this::processConnectorConstraints, db.getTable(EAConnectorConstraint.TABLE_NAME));

        iterateJETTable(sha1, now, this::processDiagramObjects, db.getTable(EADiagramObject.TABLE_NAME));
        iterateJETTable(sha1, now, this::processDiagramLinks, db.getTable(EADiagramLink.TABLE_NAME));

        iterateJETTable(sha1, now, this::processOperations, db.getTable(EAOperation.TABLE_NAME));
        iterateJETTable(sha1, now, this::processOperationTags, db.getTable(EAOperationTag.TABLE_NAME));
        iterateJETTable(sha1, now, this::processOperationParams, db.getTable(EAOperationParams.TABLE_NAME));

        iterateJETTable(sha1, now, this::processAttributes, db.getTable(EAAttribute.TABLE_NAME));
        iterateJETTable(sha1, now, this::processAttributeTags, db.getTable(EAAttributeTag.TABLE_NAME));
        iterateJETTable(sha1, now, this::processAttributeConstraints, db.getTable(EAAttributeConstraint.TABLE_NAME));

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
