import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonMapperTest {

    @Test
    public void testObject2JsonConversion() {
        Object testObject = new Object();
        String json = JsonMapper.json(testObject);
        assertNotNull(json, "JSON string should not be null");
        assertEquals("{}", json, "An Object class must {}");
    }

    @Test
    public void testJson2ObjectConversion() {
        String testJson = "{}";
        Object object = JsonMapper.object(testJson, Object.class);
        assertNotNull(object, "output object should not be null");
    }

}
