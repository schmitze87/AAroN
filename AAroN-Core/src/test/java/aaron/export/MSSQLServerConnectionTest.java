package aaron.export;

import aaron.logging.Logger;
import aaron.model.Model;
import aaron.sparx.SparxMSSQLConverter;
import aaron.sparx.TaggedValueMode;
import aaron.sparx.config.Config;
import aaron.sparx.config.DBToImport;
import aaron.sparx.config.MSSQLDB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class MSSQLServerConnectionTest {

    @Disabled
    @Test
    void connectionTest1() throws IOException {
        org.slf4j.Logger logger1 = LoggerFactory.getLogger(MSSQLServerConnectionTest.class);
        Logger logger = new TestLogger(logger1);
        Config config = new Config();
        config.setTaggedValueMode(TaggedValueMode.AS_PROPERTY);
        List<DBToImport> dbsToImport = config.getDbsToImport();
        MSSQLDB dbToImport = new MSSQLDB();
        dbToImport.setHostname("");
        dbToImport.setPort(1433);
        dbToImport.setDatabase(""); //:TODO DB needs to be specified
        dbToImport.setUsername(""); //:TODO username needs to be provided
        dbToImport.setPassword(""); //:TODO password needs to be provided
        dbToImport.setTrustCertificate(true);
        dbToImport.setAuthenticationType(MSSQLDB.AuthenticationType.AD_LOGIN);
        dbsToImport.add(dbToImport);
        Assertions.assertNotNull(config);

        SparxMSSQLConverter converter = new SparxMSSQLConverter(config, dbToImport, logger);
        Model model = converter.convert();
        Assertions.assertNotNull(model);
    }

    @Disabled
    @Test
    void connectionTest2() throws IOException {
        Logger logger = new TestLogger(LoggerFactory.getLogger(MSSQLServerConnectionTest.class));
        Config config = new Config();
        config.setTaggedValueMode(TaggedValueMode.AS_PROPERTY);
        List<DBToImport> dbsToImport = config.getDbsToImport();
        MSSQLDB dbToImport = new MSSQLDB();
        dbToImport.setHostname("localhost");
        dbToImport.setPort(1433);
        dbToImport.setDatabase(""); //:TODO DB needs to be specified
        dbToImport.setUsername(""); //:TODO username needs to be provided
        dbToImport.setPassword(""); //:TODO password needs to be provided
        dbToImport.setTrustCertificate(true);
        dbToImport.setAuthenticationType(MSSQLDB.AuthenticationType.SQL_LOGIN);
        dbsToImport.add(dbToImport);
        Assertions.assertNotNull(config);

        SparxMSSQLConverter converter = new SparxMSSQLConverter(config, dbToImport, logger);
        Model model = converter.convert();
        Assertions.assertNotNull(model);
    }
}
