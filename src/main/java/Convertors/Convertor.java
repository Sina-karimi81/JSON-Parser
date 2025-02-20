package Convertors;

import java.lang.reflect.Field;
import java.util.*;

public abstract class Convertor {

    public static String writeAsString(Object object) {
        StringBuilder result = new StringBuilder();
        if (checkForObjectType(object)) {
            handleNestedObjects(object, result);
        } else {
            handleNonNestedObjects(object, result);
        }
        return result.toString();
    }

    protected static void handleNestedObjects(Object object, StringBuilder result) {
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
                try {
                    handleNestedObjects(field.get(object), result);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (iterator.hasNext()) {
                    result.append(",");
                }
                continue;
            } else if (convertor == null) {
                throw new IllegalArgumentException("cannot convert the given field of the input due to type not being supported: " + field.getName());
            }

            convertor.marshal(object, field, result);

            if (iterator.hasNext()) {
                result.append(",");
            }
        }
        result.append("}");
    }

    protected static void handleNonNestedObjects(Object object, StringBuilder result) {
        if (isPrimitiveOrPrimitiveWrapperOrString(object.getClass())) {
            FieldType.PRIMITIVE.getConvertor().marshal(object, null, result);
        } else {
            FieldType.COLLECTION.getConvertor().marshal(object, null, result);
        }
    }

    private static Convertor getConvertor(Field field) {
        if (isPrimitiveOrPrimitiveWrapperOrString(field.getType())) {
            return FieldType.PRIMITIVE.getConvertor();
        } else if (field.getType().isArray() || Collection.class.isAssignableFrom(field.getType()) || Map.class.isAssignableFrom(field.getType())) {
            return FieldType.COLLECTION.getConvertor();
        }

        return null;
    }

    protected static String appendElement(Field field) {
        return "\"" + field.getName() + "\"";
    }

    protected static boolean isPrimitiveOrPrimitiveWrapperOrString(Class<?> type) {
        return (type.isPrimitive() && type != void.class) || isWrapperType(type) ||
                 type == String.class || type == Date.class;
    }

    protected static boolean isWrapperType(Class<?> type) {
        return type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class;
    }

    protected static boolean checkForObjectType(Object object) {
        return !object.getClass().isArray() && !(object instanceof Collection<?>) && !(object instanceof Map<?, ?>)
                && !isPrimitiveOrPrimitiveWrapperOrString(object.getClass());
    }

    public abstract void marshal(Object object, Field field, StringBuilder json);

}
