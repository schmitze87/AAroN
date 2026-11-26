package aaron.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertyDefinitionTest {

    @Test
    void testEquals() {
        var def1 = new PropertyDefinition("a", PropertyType.STRING);
        var def2 = new PropertyDefinition("a", PropertyType.STRING);
        assertEquals(def1, def2);
    }
}