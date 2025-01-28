package Convertors;

import java.lang.reflect.Field;
import java.util.*;

public abstract class Convertor {

    private static final Map<FieldType, Convertor> CONVERTORS = Map.of(FieldType.PRIMITIVE, new PrimitiveTypeConvertor());

    public static <T> String writeAsString(T object) {
        StringBuilder result = new StringBuilder();
        Field[] declaredFields = object.getClass().getDeclaredFields();

        Iterator<Field> iterator = Arrays.stream(declaredFields).iterator();
        while (iterator.hasNext()) {
            Field field = iterator.next();
            field.setAccessible(true);

            Convertor convertor = getConvertor(field);
            if (convertor == null) {
                throw new IllegalArgumentException("cannot convert the given field of the input due to not being supported: " + field.getName());
            }

            convertor.convert(object, field, result);

            if (iterator.hasNext()) {
                result.append(",").append("\n");
            }
        }

        if (!result.isEmpty()) {
            result.append("\n");
        }
        return result.toString();
    }

    private static Convertor getConvertor(Field field) {
        if (field.getType().isPrimitive()) {
            return CONVERTORS.get(FieldType.PRIMITIVE);
        }

        return null;
    }

    public abstract <T> void convert(T object, Field field, StringBuilder json);

}
