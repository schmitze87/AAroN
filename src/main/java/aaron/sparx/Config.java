package aaron.sparx;

import java.util.Map;

public class Config {

    private TaggedValueMode taggedValueMode = TaggedValueMode.AS_PROPERTY;

    public TaggedValueMode getTaggedValueMode() {
        return taggedValueMode;
    }

    static Config createFromMap(final Map<String, Object> map) {
        Config config = new Config();
        if (map != null) {
            Object taggedValues = map.getOrDefault("taggedValues", "AS_PROPERTY");
            if (taggedValues != null && taggedValues instanceof String) {
                config.taggedValueMode = TaggedValueMode.valueOf(((String) taggedValues).toUpperCase());
            }
        }
        return config;
    }
}
