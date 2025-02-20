import TestObjects.*;
import TestObjects.ultimate.Product;
import TestObjects.ultimate.QueryResult;
import TestObjects.ultimate.TestObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class JsonMapperMarshallingTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testEmptyObject2JsonConversion() {
        Object testObject = new Object();

        String json = JsonMapper.json(testObject);

        assertNotNull(json, "JSON string should not be null");
        assertEquals("{}", json, "An Object class must {}");
    }

    @Test
    public void testObject2JsonConversion_NullObjects() throws JsonProcessingException {
        String json = JsonMapper.json(null);

        String expected = mapper.writeValueAsString(null);
        assertNotNull(json, "JSON string should not be null");
        assertEquals(expected, json, "the output did not match the expected result");
    }

    @ParameterizedTest(name = "Iteration #{index} -> int = {0}, boolean = {1} and float value is {2}")
    @CsvSource({
            "34, true, 6.789f",
            "0, true, 6.789f",
            "34, false, 6.789f",
            "34, true, 0.0f",
    })
    public void testObject2JsonWithPrimitiveTypeConversion(int someInt, boolean someBool, float someFloat) throws JsonProcessingException {
        PrimitiveTypes primitiveTypes = new PrimitiveTypes(someInt, someBool, someFloat);

        String json = JsonMapper.json(primitiveTypes);

        String expected = mapper.writeValueAsString(primitiveTypes);
        assertNotNull(json, "JSON string should not be null");
        assertEquals(expected, json, "the output did not match the expected result");
    }

    @ParameterizedTest(name = "Iteration #{index} -> ints = {0}, booleans = {1} and floats value is {2}")
    @MethodSource(value = "objectArrayDataProvider")
    public void testObject2JsonWithArrayConversion(int[] someInts, boolean[] someBools, float[] someFloats) throws JsonProcessingException {
        ArrayTypes arrayTypes = new ArrayTypes();
        arrayTypes.setSomeInts(someInts);
        arrayTypes.setSomeBools(someBools);
        arrayTypes.setSomeFloats(someFloats);

        String json = JsonMapper.json(arrayTypes);

        String expected = mapper.writeValueAsString(arrayTypes);
        assertNotNull(json, "JSON string should not be null");
        assertEquals(expected, json, "the output did not match the expected result");
    }

    private static Stream<Arguments> objectArrayDataProvider() {
        return Stream.of(
                Arguments.of(new int[] {1,2,3,4}, new boolean[] {true, false}, new float[] {1.43f, 16.4324f}),
                Arguments.of(null, new boolean[] {true, false}, new float[] {1.43f, 16.4324f}),
                Arguments.of(new int[] {1,2,3,4}, null, new float[] {1.43f, 16.4324f}),
                Arguments.of(new int[] {1,2,3,4}, new boolean[] {true, false}, null)
        );
    }

    @ParameterizedTest(name = "Iteration #{index} -> array = {0}")
    @MethodSource(value = "arrayDataProvider")
    public void testArray2JsonConversion(Container<?> arrayContainer) throws JsonProcessingException {
        String json = JsonMapper.json(arrayContainer.getData());

        String expected = mapper.writeValueAsString(arrayContainer.getData());
        assertNotNull(json, "JSON string should not be null");
        assertEquals(expected, json, "the output did not match the expected result");
    }

    private static Stream<Arguments> arrayDataProvider() {
        int[] ints = {1,2,3,4};
        boolean[] booleans = {true , false};
        String[] strings = {"test1", "test2", "test3"};
        float[] floats = {1.43f, 16.4324f};

        return Stream.of(
                Arguments.of(new Container<>(ints) ),
                Arguments.of(new Container<>(booleans)),
                Arguments.of(new Container<>(floats)),
                Arguments.of(new Container<>(strings)),
                Arguments.of(new Container<>(new int[]{})),
                Arguments.of(new Container<>(null))
        );
    }

    @ParameterizedTest(name = "Iteration #{index} -> array = {0}")
    @MethodSource(value = "collectionDataProvider")
    public void testCollection2JsonConversion(Container<?> arrayContainer) throws JsonProcessingException {
        String json = JsonMapper.json(arrayContainer.getData());

        String expected = mapper.writeValueAsString(arrayContainer.getData());
        assertNotNull(json, "JSON string should not be null");
        assertEquals(expected, json, "the output did not match the expected result");
    }

    private static Stream<Arguments> collectionDataProvider() {
        List<Integer> integers = List.of(1, 2, 3, 4);
        Set<Boolean> booleans = Set.of(true, false);
        Queue<String> queue = new LinkedList<>();
        queue.add("test1");
        queue.add("test2");
        Map<String, Float> map = Map.of("float1", 1.63f, "float2", 15323432f);

        return Stream.of(
                Arguments.of(new Container<>(integers) ),
                Arguments.of(new Container<>(booleans)),
                Arguments.of(new Container<>(queue)),
                Arguments.of(new Container<>(map)),
                Arguments.of(new Container<>(List.of())),
                Arguments.of(new Container<>(null))
        );
    }

    @ParameterizedTest(name = "Iteration #{index} -> arrays = {0}, primitives = {1}")
    @MethodSource(value = "JsonMapperMarshallingTest#compoundDataProvider")
    public void testObject2JsonWithCompoundConversion(ArrayTypes arrayTypes, PrimitiveTypes primitiveTypes) throws JsonProcessingException {
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
                Arguments.of(
                        new ArrayTypes(new int[] {1,2,3,4}, new boolean[] {true, false}, new float[] {1.43f, 16.4324f}),
                        new PrimitiveTypes(34, true, 13.44f)
                ),
                Arguments.of(
                        new ArrayTypes(null, new boolean[] {true, false}, new float[] {1.43f, 16.4324f}),
                        new PrimitiveTypes(0 , true, 13.44f)
                ),
                Arguments.of(
                        new ArrayTypes(new int[] {1,2,3,4}, null, new float[] {1.43f, 16.4324f}),
                        new PrimitiveTypes(13 , false, 13.44f)
                ),
                Arguments.of(
                        new ArrayTypes(new int[] {1,2,3,4}, new boolean[] {true, false}, null),
                        new PrimitiveTypes(0 , true, 0.0f)
                )
        );
    }

    @ParameterizedTest(name = "Iteration #{index} -> collection = {0}, Map = {1}, Set = {2}, Queue = {3}")
    @MethodSource(value = "multiCollectionDataProvider")
    public void testObject2JsonWithCollectionTypesConversion(List<String> list, Map<String, Integer> map, Set<Integer> set, Queue<Integer> queue) throws JsonProcessingException {
        CollectionTypes collectionTypes = new CollectionTypes(list, map, set, queue);

        String json = JsonMapper.json(collectionTypes);

        String expected = mapper.writeValueAsString(collectionTypes);
        assertNotNull(json, "JSON string should not be null");
        assertEquals(expected, json, "the output did not match the expected result");
    }

    private static Stream<Arguments> multiCollectionDataProvider() {
        List<String> testList = new ArrayList<>();
        testList.add("test");
        testList.add(null);

        Map<String, Integer> testMap = new HashMap<>();
        testMap.put("key1", 69);
        testMap.put("420", null);

        return Stream.of(
                Arguments.of(List.of("test1", "test2", "test23"), Map.of("key1", 1, "key2",  2), null, null),
                Arguments.of(null, Map.of("key1", 1, "key2",  2), null, null),
                Arguments.of(List.of("test1", "test2", "test23"), null, null, null),
                Arguments.of(List.of(), Map.of(), null, null),
                Arguments.of(testList, testMap, null, null),
                Arguments.of(null, null, Set.of(1,2,3,4), new PriorityQueue<>(Set.of(1,2,3,4))),
                Arguments.of(testList, testMap, Set.of(1,2,3,4), new PriorityQueue<>(Set.of(1,2,3,4)))
        );
    }

    @Test
    public void testDate2JsonConversion() throws JsonProcessingException {
        Date date = new Date();
        String json = JsonMapper.json(date);

        String expected = mapper.writeValueAsString(date);
        assertNotNull(json, "JSON string should not be null");
        assertEquals(expected, json, "the output of simple date did not match the expected result");

        DateType dateType = new DateType(date);
        String jsonned = JsonMapper.json(dateType);

        String written = mapper.writeValueAsString(dateType);
        assertNotNull(jsonned, "JSON string should not be null");
        assertEquals(written, jsonned, "the output of instance variable date did not match the expected result");
    }

    @Test
    public void testObject2JsonConversion() throws JsonProcessingException {
        Product product = new Product("test1", 5.43f);
        QueryResult result = new QueryResult(List.of("ps5", "xbox", "nintendo"), 3);

        TestObject testObject = new TestObject(result, product, new Date());
        String expected = mapper.writeValueAsString(testObject);

        String jsonned = JsonMapper.json(testObject);

        assertNotNull(jsonned, "JSON string should not be null");
        assertEquals(expected, jsonned, "the output of instance variable date did not match the expected result");
    }

    @Test
    public void testListObjects2JsonConversion() throws JsonProcessingException {
        Product product1 = new Product("PS5", 6.43f);
        Product product2 = new Product("XBOX", 4.00f);
        Product product3 = new Product("Switch", 3.5f);
        List<Product> products = List.of(product1, product2, product3);

        String jsonned = JsonMapper.json(products);

        String expected = mapper.writeValueAsString(products);
        assertNotNull(jsonned, "JSON string should not be null");
        assertEquals(expected, jsonned, "the output of instance variable date did not match the expected result");

        Integer integer1 = 1;
        Integer integer2 = 2;
        Integer integer3 = 3;
        List<Integer> integers = List.of(integer1, integer2, integer3);

        jsonned = JsonMapper.json(integers);

        expected = mapper.writeValueAsString(integers);
        assertNotNull(jsonned, "JSON string should not be null");
        assertEquals(expected, jsonned, "the output of instance variable date did not match the expected result");
    }
}
