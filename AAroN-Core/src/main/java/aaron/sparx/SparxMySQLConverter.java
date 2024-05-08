package aaron.sparx;

import aaron.model.Model;
import aaron.sparx.model.*;
import aaron.util.Util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class SparxMySQLConverter extends AbstractSparxConverter {

    private final String host;
    private final long port;
    private final String databaseName;
    private final String username;
    private final String password;

    public SparxMySQLConverter(final Config config, String host, long port, String databaseName, String username, String password) {
        super(new Model(), config);
        this.config = config;
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }

    @Override
    public Model convert() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?user=" + username + "&password=" + password;
        try (Connection connection = DriverManager.getConnection(url)) {
            String sha1 = Util.createSHA1(url, now.toInstant(ZoneOffset.UTC));
            context.setFileHash(sha1);

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
            return model;
        } catch (SQLException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
