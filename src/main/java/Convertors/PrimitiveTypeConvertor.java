package Convertors;

import java.lang.reflect.Field;

public class PrimitiveTypeConvertor extends Convertor {

    @Override
    public <T> void convert(T object, Field field, StringBuilder json) {
        try {
            json.append(appendElement(field))
                    .append(":")
                    .append(field.get(object));
        } catch (IllegalAccessException e) {
            System.out.println("could not access the provided field: " + field.getName());
            throw new RuntimeException(e);
        }
    }

}
