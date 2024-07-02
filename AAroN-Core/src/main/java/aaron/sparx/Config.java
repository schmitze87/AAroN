package aaron.sparx;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Config {

    private TaggedValueMode taggedValueMode = TaggedValueMode.AS_PROPERTY;
    private List<String> filesToConvert = new ArrayList<>();

    public TaggedValueMode getTaggedValueMode() {
        return taggedValueMode;
    }

    public List<String> getFilesToConvert() {
        return filesToConvert;
    }

    public void setFilesToConvert(List<String> filesToConvert) {
        this.filesToConvert = filesToConvert;
    }

    public static Config createFromMap(final Map<String, Object> map) {
        Config config = new Config();
        if (map != null) {
            Object taggedValues = map.getOrDefault("taggedValues", "AS_PROPERTY");
            if (taggedValues != null && taggedValues instanceof String) {
                config.taggedValueMode = TaggedValueMode.valueOf(((String) taggedValues).toUpperCase());
            }
        }
        return config;
    }

    public static Config loadFromYAML(File configFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.findAndRegisterModules();
        Config config = mapper.readValue(configFile, Config.class);
        return config;
    }
}
