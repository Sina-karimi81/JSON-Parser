package Convertors;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class CollectionTypeConvertor extends Convertor {

    @Override
    public void marshal(Object object, Field field, StringBuilder json) {
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

            collection.forEach(o -> result.append(createValue(o)).append(","));
            if (!collection.isEmpty()) {
                result.deleteCharAt(result.length() - 1);
            }

            result.append("]");
        } else if (input instanceof Map<?,?> map) {
            result.append("{");

            map.forEach((k, v) -> result.append("\"").append(k).append("\":").append(createValue(v)).append(","));
            if (!map.isEmpty()) {
                result.deleteCharAt(result.length() - 1);
            }

            result.append("}");
        } else if (input.getClass().isArray()) {
            result.append("[");

            for (int i = 0; i < Array.getLength(input); i++) {
                Object o = Array.get(input, i);
                result.append(createValue(o)).append(",");
            }

            if (Array.getLength(input) != 0) {
                result.deleteCharAt(result.length()-1);
            }

            result.append("]");
        }

        return result.toString();
    }

    private String createValue(Object object) {
        StringBuilder result = new StringBuilder();

        if (object == null) {
            return "null";
        }

        if (object instanceof String) {
            return result.append("\"").append(object).append("\"").toString();
        }

        if (isPrimitiveOrPrimitiveWrapperOrString(object.getClass())) {
            handleNonNestedObjects(object, result);
            return result.toString();
        }

        handleNestedObjects(object, result);
        return result.toString();
    }

}
