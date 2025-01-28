import TestObjects.PrimitiveTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonMapperMarshallingTest {

    @Test
    public void testObject2JsonConversion() {
        Object testObject = new Object();
        String json = JsonMapper.json(testObject);
        assertNotNull(json, "JSON string should not be null");
        assertEquals("{\n}", json, "An Object class must {}");
        System.out.println(json);
    }

    @Test
    public void testObject2JsonConversion_NullObjects() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> JsonMapper.json(null));
        assertEquals("Cannot convert a null input parameter", illegalArgumentException.getMessage());
    }

    @Test
    public void testObject2JsonPrimitiveTypeConversion() {
        String resultJson = "{\n\"someInt\": 34,\n\"someBool\": true,\n\"someFloat\": 6.789\n}";
        PrimitiveTypes primitiveTypes = new PrimitiveTypes(34, true, 6.789f);
        String json = JsonMapper.json(primitiveTypes);
        assertNotNull(json, "JSON string should not be null");
        assertEquals(resultJson, json, "the output did not match the expected result");
        System.out.println(json);
    }

}
