package aaron.sparx;

import aaron.model.Model;
import aaron.sparx.model.*;
import aaron.util.Util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class SparxSQLiteConverter extends AbstractSparxConverter {

    private File qeaFile;

    public SparxSQLiteConverter(final Config config, final File qeaFile) {
        super(new Model(), config);
        this.config = config;
        this.qeaFile = qeaFile;
    }

    @Override
    public Model convert() throws IOException {
        LocalDateTime now = LocalDateTime.now();

        String sha1 = null;
        try {
            sha1 = Util.createSHA256(qeaFile);
            context.setFileHash(sha1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + qeaFile.getAbsolutePath())) {
            handleTable(sha1, now, connection, this::processSystem, "SELECT * FROM " + EASystem.TABLE_NAME);

            handleTable(sha1, now, connection, this::processPackages, "SELECT * FROM " + EAPackage.TABLE_NAME);

            handleTable(sha1, now, connection, this::processDiagrams, "SELECT * FROM " + EADiagram.TABLE_NAME);

            handleTable(sha1, now, connection, this::processObjects, "SELECT * FROM " + EAObject.TABLE_NAME);
            handleTable(sha1, now, connection, this::processObjectProperties, "SELECT * FROM " + EAObjectProperty.TABLE_NAME);
            handleTable(sha1, now, connection, this::processObjectConstraints, "SELECT * FROM " + EAObjectConstraint.TABLE_NAME);

            handleTable(sha1, now, connection, this::processConnectors, "SELECT * FROM " + EAConnector.TABLE_NAME);
            handleTable(sha1, now, connection, this::processConnectorTags, "SELECT * FROM " + EAConnectorTag.TABLE_NAME);
            handleTable(sha1, now, connection, this::processConnectorConstraints, "SELECT * FROM " + EAConnectorConstraint.TABLE_NAME);

            handleTable(sha1, now, connection, this::processDiagramObjects, "SELECT * FROM " + EADiagramObject.TABLE_NAME);
            handleTable(sha1, now, connection, this::processDiagramLinks, "SELECT * FROM " + EADiagramLink.TABLE_NAME);

            handleTable(sha1, now, connection, this::processOperations, "SELECT * FROM " + EAOperation.TABLE_NAME);
            handleTable(sha1, now, connection, this::processOperationTags, "SELECT * FROM " + EAOperationTag.TABLE_NAME);
            handleTable(sha1, now, connection, this::processOperationParams, "SELECT * FROM " + EAOperationParams.TABLE_NAME);

            handleTable(sha1, now, connection, this::processAttributes, "SELECT * FROM " + EAAttribute.TABLE_NAME);
            handleTable(sha1, now, connection, this::processAttributeTags, "SELECT * FROM " + EAAttributeTag.TABLE_NAME);
            handleTable(sha1, now, connection, this::processAttributeConstraints, "SELECT * FROM " + EAAttributeConstraint.TABLE_NAME);

            handleTable(sha1, now, connection, this::processXRefs, "SELECT * FROM " + EAXref.TABLE_NAME);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return model;
    }
}
