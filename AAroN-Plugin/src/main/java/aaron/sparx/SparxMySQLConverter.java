package aaron.sparx;

import aaron.model.Model;
import aaron.sparx.model.*;
import aaron.util.Util;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SparxMySQLConverter extends AbstractSparxConverter {

    private Log log;
    private final String host;
    private final long port;
    private final String databaseName;
    private final String username;
    private final String password;

    public SparxMySQLConverter(Log log, final Config config, String host, long port, String databaseName, String username, String password) {
        super(new Model(), config);
        this.log = log;
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
        model = new Model();
        String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?user=" + username + "&password=" + password;
        try (Connection connection = DriverManager.getConnection(url)) {
            String sha1 = Util.createSHA1(url, now.toInstant(ZoneOffset.UTC));

            handleTable(sha1, now, connection, this::processPackages, "SELECT * FROM " + EAPackage.TABLE_NAME);

            handleTable(sha1, now, connection, this::processDiagrams, "SELECT * FROM " + EADiagram.TABLE_NAME);

            handleTable(sha1, now, connection, this::processObjects, "SELECT * FROM " + EAObject.TABLE_NAME);
            handleTable(sha1, now, connection, this::processObjectProperties, "SELECT * FROM " + EAObjectProperty.TABLE_NAME);
            handleTable(sha1, now, connection, this::processObjectConstraints, "SELECT * FROM " + EAObjectConstraint.TABLE_NAME);

            handleTable(sha1, now, connection, this::processConnectors, "SELECT * FROM " + EAConnector.TABLE_NAME);
            handleTable(sha1, now, connection, this::processConnectorTags, "SELECT * FROM " + EAConnectorTag.TABLE_NAME);
            handleTable(sha1, now, connection, this::processConnectorConstraints, "SELECT * FROM " + EAConnectorConstraint.TABLE_NAME);

            handleTable(sha1, now, connection, this::processOperations, "SELECT * FROM " + EAOperation.TABLE_NAME);
            handleTable(sha1, now, connection, this::processOperationTags, "SELECT * FROM " + EAOperationTag.TABLE_NAME);
            handleTable(sha1, now, connection, this::processOperationParams, "SELECT * FROM " + EAOperationParams.TABLE_NAME);

            handleTable(sha1, now, connection, this::processAttributes, "SELECT * FROM " + EAAttribute.TABLE_NAME);
            handleTable(sha1, now, connection, this::processAttributeTags, "SELECT * FROM " + EAAttributeTag.TABLE_NAME);
            handleTable(sha1, now, connection, this::processAttributeConstraints, "SELECT * FROM " + EAAttributeConstraint.TABLE_NAME);
            return model;
        } catch (SQLException e) {
            log.error("SQLException: " + e.getMessage());
            log.error("SQLState: " + e.getSQLState());
            log.error("VendorError: " + e.getErrorCode());
            log.error("Could not create connection to database", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("", e);
        }
        return null;
    }

    private void handleTable(String sha1, LocalDateTime now, Connection connection, ProcessInterface processInterface, String sql) throws SQLException {
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            processInterface.process(sha1, now, handleResultSet(resultSet));
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ignored) { } // ignore
                resultSet = null;
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignored) { } // ignore
                statement = null;
            }
        }
    }

    private List<Map<String, Object>> handleResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<Map<String, Object>> queryData = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> rowData = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(metaData.getColumnName(i), rs.getObject(i));
            }
            queryData.add(rowData);
        }
        return queryData;
    }

    @FunctionalInterface
    private interface ProcessInterface {
        <T extends Iterable<U>, U extends Map<String, Object>> void process(final String sha1, final LocalDateTime time, final T table);
    }

}
