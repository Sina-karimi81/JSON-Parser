import TestObjects.ultimate.QueryResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.JsonParseException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonMapperUnmarshallingTest {

    @Test
    public void debugJacksonReadValue() throws JsonParseException, JsonProcessingException {
        String content = "{\"list\": [\"Hello\",\"World\"], \"count\": 2}";

        ObjectMapper mapper = new ObjectMapper();

        QueryResult result = mapper.readValue(content, QueryResult.class);
        assertNotNull(result);
        assertEquals(List.of("Hello","World"), result.getList());
        assertEquals(2, result.getCount());
    }

    @Test
    public void testObjectWithListTypes() throws JsonParseException {
        String content = "{\"list\": [\"Hello\",\"World\"], \"count\": 2}";

        QueryResult result = JsonMapper.object(content, QueryResult.class);
        assertNotNull(result);
        assertEquals(List.of("Hello","World"), result.getList());
        assertEquals(2, result.getCount());
    }

    @Test
    public void getGenericTypeInfo() {
        List<String> strings = new ArrayList<>();
        Class<?> clazz = strings.getClass();
        TypeVariable<? extends Class<?>>[] typeParameters = clazz.getTypeParameters();
        Class<?> componentType = clazz.getComponentType();
        Type genericSuperclass = clazz.getGenericSuperclass();

        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) genericSuperclass;
            Type actualType = paramType.getActualTypeArguments()[0];
            System.out.println("Actual Type: " + actualType.getTypeName()); // java.lang.String
        }
    }

}
