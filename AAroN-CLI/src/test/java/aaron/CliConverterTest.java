package aaron;

import aaron.sparx.config.Config;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;

class CliConverterTest {

    @TempDir(cleanup = CleanupMode.NEVER)
    File tempDir;

    @Test
    void testMSSQL() throws IOException, AAroNConversionException {
        //TODO: Not yet ready...
//        var mssqlserver = new MSSQLServerContainer(DockerImageName.parse())
//                .acceptLicense()
//                .ini;
//        mssqlserver.start();
//
//        ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
//                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.findAndRegisterModules();
//        Config config = mapper.readValue(new File("src/test/resources/aaron_db_config.yml"), Config.class);
//        Assertions.assertNotNull(config);
//
//        CliConverter cliConverter = new CliConverter();
//        cliConverter.outputDir =  tempDir;
//        cliConverter.convert(config, System.out);
    }
}