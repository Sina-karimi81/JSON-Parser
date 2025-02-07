package Convertors;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Iterator;

public class ArrayTypeConvertor extends Convertor {

    @Override
    public <T> void convert(T object, Field field, StringBuilder json) {
        try {
            json.append(appendElement(field))
                    .append(":")
                    .append(createArrayString(field.get(object)));
        } catch (IllegalAccessException e) {
            System.out.println("could not access the provided field: " + field.getName());
            throw new RuntimeException(e);
        }
    }

    private String createArrayString(Object input) {
        if (input == null) {
            return "null";
        }

        StringBuilder result = new StringBuilder("[");

        for (int i = 0; i < Array.getLength(input); i++) {
            Object o = Array.get(input, i);
            result.append(o).append(",");
        }

        result.deleteCharAt(result.length()-1).append("]");

        return result.toString();
    }
}
