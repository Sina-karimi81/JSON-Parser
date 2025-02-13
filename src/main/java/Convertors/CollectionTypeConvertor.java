package Convertors;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class CollectionTypeConvertor extends Convertor {

    @Override
    public <T> void convert(T object, Field field, StringBuilder json) {
        if (field == null) {
            String collectionString = createCollectionString(object);
            json.append(collectionString);
            return;
        }

        try {
            json.append(appendElement(field))
                    .append(":")
                    .append(createCollectionString(field.get(object)));
        } catch (IllegalAccessException e) {
            System.out.println("could not access the provided field: " + field.getName());
            throw new RuntimeException(e);
        }
    }

    private String createCollectionString(Object input) {
        if (input == null) {
            return "null";
        }

        StringBuilder result = new StringBuilder();

        if (input instanceof Collection<?> collection) {
            result.append("[");

            collection.forEach(o -> result.append(createValueForString(o)).append(","));
            if (!collection.isEmpty()) {
                result.deleteCharAt(result.length() - 1);
            }

            result.append("]");
        } else if (input instanceof Map<?,?> map) {
            result.append("{");

            map.forEach((k, v) -> result.append("\"").append(k).append("\":").append(createValueForString(v)).append(","));
            if (!map.isEmpty()) {
                result.deleteCharAt(result.length() - 1);
            }

            result.append("}");
        }

        return result.toString();
    }

    private Object createValueForString(Object object) {
        if (object instanceof String) {
            return "\"" + object + "\"";
        }

        return object;
    }

}
