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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

class CliConverterTest {

    @TempDir(cleanup = CleanupMode.DEFAULT)
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

    @Test
    void testEmptyConfig() throws IOException, AAroNConversionException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.findAndRegisterModules();
        Config config = mapper.readValue(new File("src/test/resources/aaron_config_empty.yml"), Config.class);
        Assertions.assertNotNull(config);

        CliConverter cliConverter = new CliConverter();
        cliConverter.outputDir =  tempDir;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        cliConverter.convert(config, outputStream);
        AAronCLIOutput output = mapper.readValue(outputStream.toByteArray(), AAronCLIOutput.class);
        Assertions.assertNotNull(output);
        Assertions.assertTrue(output.getNodesToImport().isEmpty());
        Assertions.assertTrue(output.getEdgesToImport().isEmpty());
    }

    @Test
    void testCLICommand() throws IOException {
        File file = new File("src/test/resources/aaron_config_empty.yml");
        int exitCode = AAroNCLI.execute(new String[]{"convert", "-o", tempDir.getAbsolutePath(), "-c", file.getAbsolutePath()});
        Assertions.assertEquals(0, exitCode);
        File aaronOutputYML = tempDir.toPath().resolve("aaron_output.yml").toFile();
        Assertions.assertTrue(aaronOutputYML.exists(), "AAroN Output YML does not exist");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        AAronCLIOutput output = mapper.readValue(aaronOutputYML, AAronCLIOutput.class);
        Assertions.assertNotNull(output);
        Assertions.assertTrue(output.getNodesToImport().isEmpty());
        Assertions.assertTrue(output.getEdgesToImport().isEmpty());
        aaronOutputYML.delete();
    }
}