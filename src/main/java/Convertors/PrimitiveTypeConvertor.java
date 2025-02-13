package Convertors;

import java.lang.reflect.Field;
import java.util.Date;

public class PrimitiveTypeConvertor extends Convertor {

    @Override
    public <T> void convert(T object, Field field, StringBuilder json) {
        try {
            Object obj = field.get(object);
            json.append(appendElement(field))
                    .append(":");

            if (obj instanceof String) {
                json.append("\"").append(obj).append("\"");
                return;
            }

            if (obj instanceof Date date) {
                json.append(date.getTime());
                return;
            }

            json.append(obj);
        } catch (IllegalAccessException e) {
            System.out.println("could not access the provided field: " + field.getName());
            throw new RuntimeException(e);
        }
    }

}
