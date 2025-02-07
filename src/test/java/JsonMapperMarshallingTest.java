import TestObjects.ArrayTypes;
import TestObjects.CompoundTypes;
import TestObjects.PrimitiveTypes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class JsonMapperMarshallingTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testObject2JsonConversion() {
        Object testObject = new Object();

        String json = JsonMapper.json(testObject);

        assertNotNull(json, "JSON string should not be null");
        assertEquals("{}", json, "An Object class must {}");
    }

    @Test
    public void testObject2JsonConversion_NullObjects() {
        assertThrows(IllegalArgumentException.class, () -> JsonMapper.json(null));
    }

    @ParameterizedTest(name = "Iteration #{index} -> int = {0}, boolean = {1} and float value is {2}")
    @CsvSource({
            "34, true, 6.789f",
            "0, true, 6.789f",
            "34, false, 6.789f",
            "34, true, 0.0f",
    })
    public void testObject2JsonPrimitiveTypeConversion(int someInt, boolean someBool, float someFloat) throws JsonProcessingException {
        PrimitiveTypes primitiveTypes = new PrimitiveTypes(someInt, someBool, someFloat);

        String json = JsonMapper.json(primitiveTypes);

        String expected = mapper.writeValueAsString(primitiveTypes);
        assertNotNull(json, "JSON string should not be null");
        assertEquals(expected, json, "the output did not match the expected result");
    }

    @ParameterizedTest(name = "Iteration #{index} -> ints = {0}, booleans = {1} and floats value is {2}")
    @MethodSource(value = "JsonMapperMarshallingTest#arrayDataProvider")
    public void testObject2JsonArrayConversion(int[] someInts, boolean[] someBools, float[] someFloats) throws JsonProcessingException {
        ArrayTypes arrayTypes = new ArrayTypes();
        arrayTypes.setSomeInts(someInts);
        arrayTypes.setSomeBools(someBools);
        arrayTypes.setSomeFloats(someFloats);

        String json = JsonMapper.json(arrayTypes);

        String expected = mapper.writeValueAsString(arrayTypes);
        assertNotNull(json, "JSON string should not be null");
        assertEquals(expected, json, "the output did not match the expected result");
    }

    private static Stream<Arguments> arrayDataProvider() {
        return Stream.of(
                Arguments.of(new int[] {1,2,3,4}, new boolean[] {true, false}, new float[] {1.43f, 16.4324f}),
                Arguments.of(null, new boolean[] {true, false}, new float[] {1.43f, 16.4324f}),
                Arguments.of(new int[] {1,2,3,4}, null, new float[] {1.43f, 16.4324f}),
                Arguments.of(new int[] {1,2,3,4}, new boolean[] {true, false}, null)
        );
    }

    @ParameterizedTest(name = "Iteration #{index} -> arrays = {0}, primitives = {1}")
    @MethodSource(value = "JsonMapperMarshallingTest#compoundDataProvider")
    public void testObject2JsonCompoundConversion(ArrayTypes arrayTypes, PrimitiveTypes primitiveTypes) throws JsonProcessingException {
        CompoundTypes compoundTypes = new CompoundTypes();
        compoundTypes.setSomeInts(arrayTypes.getSomeInts());
        compoundTypes.setSomeBools(arrayTypes.getSomeBools());
        compoundTypes.setSomeFloats(arrayTypes.getSomeFloats());
        compoundTypes.setSomeInt(primitiveTypes.getSomeInt());
        compoundTypes.setSomeBool(primitiveTypes.isSomeBool());
        compoundTypes.setSomeFloat(primitiveTypes.getSomeFloat());

        String json = JsonMapper.json(compoundTypes);

        String expected = mapper.writeValueAsString(compoundTypes);
        assertNotNull(json, "JSON string should not be null");
        assertEquals(expected, json, "the output did not match the expected result");
    }

    private static Stream<Arguments> compoundDataProvider() {
        return Stream.of(
                Arguments.of(new ArrayTypes(new int[] {1,2,3,4}, new boolean[] {true, false}, new float[] {1.43f, 16.4324f}),
                        new PrimitiveTypes(34, true, 13.44f)),
                Arguments.of(new ArrayTypes(null, new boolean[] {true, false}, new float[] {1.43f, 16.4324f}),
                        new PrimitiveTypes(0 , true, 13.44f)),
                Arguments.of(new ArrayTypes(new int[] {1,2,3,4}, null, new float[] {1.43f, 16.4324f}),
                        new PrimitiveTypes(13 , false, 13.44f)),
                Arguments.of(new ArrayTypes(new int[] {1,2,3,4}, new boolean[] {true, false}, null),
                        new PrimitiveTypes(0 , true, 0.0f))
        );
    }

}
