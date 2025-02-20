package Convertors;

import java.lang.reflect.Field;
import java.util.*;

public abstract class Convertor {

    protected static String appendElement(Field field) {
        return "\"" + field.getName() + "\"";
    }

    public static <T> String writeAsString(T object) {
        try {
            StringBuilder result = new StringBuilder();
            if (checkForObjectType(object)) {
                handleObjects(object, result);
            } else {
                handlePrimitives(object, result);
            }
            return result.toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleObjects(Object object, StringBuilder result) throws IllegalAccessException {
        Field[] declaredFields = object.getClass().getDeclaredFields();

        result.append("{");
        Iterator<Field> iterator = Arrays.stream(declaredFields).iterator();
        while (iterator.hasNext()) {
            Field field = iterator.next();
            if (!field.canAccess(object)) {
                field.setAccessible(true);
            }

            Convertor convertor = getConvertor(field);
            if (convertor == null && checkForObjectType(field)) {
                result.append(appendElement(field)).append(":");
                handleObjects(field.get(object), result);
                if (iterator.hasNext()) {
                    result.append(",");
                }
                continue;
            } else if (convertor == null) {
                throw new IllegalArgumentException("cannot convert the given field of the input due to type not being supported: " + field.getName());
            }

            convertor.convert(object, field, result);

            if (iterator.hasNext()) {
                result.append(",");
            }
        }
        result.append("}");
    }

    private static void handlePrimitives(Object object, StringBuilder result) {
        if (object.getClass().isArray()) {
            FieldType.ARRAY.getConvertor().convert(object, null, result);
        } else if (object instanceof Date date) {
            result.append(date.getTime());
        } else {
            FieldType.COLLECTION.getConvertor().convert(object, null, result);
        }
    }

    private static Convertor getConvertor(Field field) {
        if (isPrimitiveOrPrimitiveWrapperOrString(field.getType())) {
            return FieldType.PRIMITIVE.getConvertor();
        } else if (field.getType().isArray()) {
            return FieldType.ARRAY.getConvertor();
        } else if (Collection.class.isAssignableFrom(field.getType()) || Map.class.isAssignableFrom(field.getType())) {
            return FieldType.COLLECTION.getConvertor();
        }

        return null;
    }

    public static boolean isPrimitiveOrPrimitiveWrapperOrString(Class<?> type) {
        return (type.isPrimitive() && type != void.class) || isWrapperType(type) ||
                 type == String.class || type == Date.class;
    }

    private static boolean isWrapperType(Class<?> type) {
        return type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class;
    }

    private static boolean checkForObjectType(Object object) {
        return !object.getClass().isArray() && !(object instanceof Collection<?>) && !(object instanceof Map<?, ?>)
                && !(object instanceof String) && !(object instanceof Date) && !isWrapperType(object.getClass());
    }

    public abstract <T> void convert(T object, Field field, StringBuilder json);

}
