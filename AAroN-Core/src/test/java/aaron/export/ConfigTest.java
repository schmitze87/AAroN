package aaron.export;

import aaron.sparx.Config;
import aaron.sparx.TaggedValueMode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigTest {

    @Test
    void loadConfigTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.findAndRegisterModules();
        Config config = mapper.readValue(new File("src/test/resources/aaron_config.yml"), Config.class);
        Assertions.assertNotNull(config);
        Assertions.assertEquals(TaggedValueMode.AS_PROPERTY, config.getTaggedValueMode());
        Assertions.assertTrue(List.of("bla", "blupp").containsAll(config.getFilesToImport()));
    }
}
