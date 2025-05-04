import TestObjects.pojo.Product;
import TestObjects.pojo.QueryResult;
import TestObjects.pojo.QueryResultWithArray;
import exception.JsonParseException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class JsonMapperUnmarshallingTest {

    @Test
    public void Given_JsonWithPrimitive_ExpectObject() throws JsonParseException {
        String content = "{\"name\": \"prod\", \"price\": 2.0}";

        Product result = JsonMapper.object(content, Product.class);
        assertNotNull(result);
        assertEquals("prod", result.getName());
        assertEquals(2.0f, result.getPrice());
    }

    @Test
    public void testObjectWithArrayTypes() throws JsonParseException {
        String content = "{\"list\": [\"Hello\",\"World\"], \"count\": 2}";

        QueryResultWithArray result = JsonMapper.object(content, QueryResultWithArray.class);
        assertNotNull(result);
        assertArrayEquals(new String[]{"Hello", "World"}, result.getList());
        assertEquals(2, result.getCount());
    }

    @Test
    public void testObjectWithListTypes() throws JsonParseException {
        String content = "{\"list\": [\"Hello\",\"World\"], \"set\": [1,1], \"count\": 2}";

        QueryResult result = JsonMapper.object(content, QueryResult.class);
        assertNotNull(result);
        assertEquals(List.of("Hello", "World"), result.getList());
        assertEquals(Set.of(1), result.getSet());
        assertEquals(2, result.getCount());
    }
}
