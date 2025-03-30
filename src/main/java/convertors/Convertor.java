package convertors;

import data.FieldData;

import java.lang.reflect.Field;
import java.util.*;

import static util.TypeUtils.*;

public abstract class Convertor {
    public static String writeAsString(Object object) {
        StringBuilder result = new StringBuilder();

        if (isObject(object)) {
            handleObjects(object, result);
        } else {
            handleNonObjects(object, result);
        }

        return result.toString();
    }

    protected static void handleObjects(Object object, StringBuilder result) {
        Field[] declaredFields = object.getClass().getDeclaredFields();

        result.append("{");
        Iterator<Field> iterator = Arrays.stream(declaredFields).iterator();
        while (iterator.hasNext()) {
            Field field = iterator.next();
            if (!field.canAccess(object)) {
                field.setAccessible(true);
            }

            Convertor convertor = getConvertor(field);
            Object fieldValue = getFieldValue(field, object);
            FieldData data = new FieldData(field.getName(), fieldValue);
            convertor.marshal(data, result);

            if (iterator.hasNext()) {
                result.append(",");
            }
        }
        result.append("}");
    }

    protected static void handleNonObjects(Object object, StringBuilder result) {
        FieldData data = new FieldData(null, object);
        if (isPrimitiveOrPrimitiveWrapperOrString(object.getClass())) {
            FieldType.PRIMITIVE.getConvertor().marshal(data, result);
        } else {
            FieldType.COLLECTION.getConvertor().marshal(data, result);
        }
    }

    protected static Object getFieldValue(Field field, Object parent) {
        try {
            return field.get(parent);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Convertor getConvertor(Field field) {
        if (isPrimitiveOrPrimitiveWrapperOrString(field.getType())) {
            return FieldType.PRIMITIVE.getConvertor();
        } else if (isCollectionTypeOrArray(field.getType())) {
            return FieldType.COLLECTION.getConvertor();
        } else {
            return FieldType.OBJECTS.getConvertor();
        }
    }

    protected static String appendElement(String name) {
        return "\"" + name + "\"";
    }

    public abstract void marshal(FieldData data, StringBuilder json);

}
