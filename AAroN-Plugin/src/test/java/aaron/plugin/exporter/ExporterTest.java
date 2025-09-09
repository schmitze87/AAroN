package aaron.plugin.exporter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ExporterTest {

    @Test
    public void testTypeDetection() {
        Map<String, Object> testData = new HashMap<>();
        testData.put("bool", new boolean[]{true});
        Object value = testData.get("bool");
        Class<?> valueClass = value.getClass();
        Assertions.assertEquals(boolean.class, valueClass);
    }
}
