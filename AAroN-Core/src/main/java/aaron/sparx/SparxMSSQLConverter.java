package aaron.sparx;

import aaron.logging.Logger;
import aaron.model.Model;
import aaron.sparx.config.Config;
import aaron.sparx.config.MSSQLDB;
import aaron.sparx.model.*;
import aaron.util.Util;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class SparxMSSQLConverter extends AbstractSparxConverter {

    private final String host;
    private final String instance;
    private final long port;
    private final String databaseName;
    private final String username;
    private final String password;
    private final boolean trustServerCertificate;
    private final MSSQLDB.AuthenticationType authenticationType;

    public SparxMSSQLConverter(final Config config, String host, String instance, long port, String databaseName, String username, String password, MSSQLDB.AuthenticationType authenticationType, Logger logger) {
        super(new Model(), config, logger);
        this.config = config;
        this.host = host;
        this.instance = instance;
        this.port = port;
        this.trustServerCertificate = true;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
        this.authenticationType = authenticationType;
    }

    public SparxMSSQLConverter(final Config config, MSSQLDB dbToImport, Logger logger) {
        super(new Model(), config, logger);
        this.config = config;
        this.host = dbToImport.getHostname();
        this.port = dbToImport.getPort();
        this.instance = dbToImport.getInstance();
        this.trustServerCertificate = dbToImport.isTrustCertificate();
        this.databaseName = dbToImport.getDatabase();
        this.username = dbToImport.getUsername();
        this.password = dbToImport.getPassword();
        this.authenticationType = dbToImport.getAuthenticationType();
    }

    @Override
    public Model convert() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        //jdbc:sqlserver://[serverName[\instanceName][:portNumber]][;property=value[;property=value]]
        String url = "jdbc:sqlserver://" + host + (StringUtils.isNotBlank(instance) ? "\\" +instance : "") + ":" + port + ";databaseName=" + databaseName;
        switch (authenticationType) {
            case AD_LOGIN ->   url = url + ";authenticationScheme=NTLM;integratedSecurity=true";
            case SQL_LOGIN ->  url = url + ";authenticationScheme=NativeAuthentication;integratedSecurity=false";
        }
        String secureUrl = url + ";encrypt=true" + ";trustServerCertificate=" + trustServerCertificate;
        String semiSecureUrl = url + ";encrypt=true" + ";trustServerCertificate=true";
        String[] urls = new String[]{secureUrl, semiSecureUrl, url};
        for (int i = 0; i < urls.length; i++) {
            if (i == 0) {
                logger.info("try to establish secure connection to {}", host);
            } else {
                logger.warn("Could not establish secure connection to {}. Try insecure connection", host);
            }
            String jdbcUrl = urls[i];
            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                String sha1 = Util.createSHA1(jdbcUrl, now.toInstant(ZoneOffset.UTC));
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
            } catch (SQLServerException serverException) {
                continue;
            } catch (SQLException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
