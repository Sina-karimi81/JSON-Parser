package util;

import exception.JsonParseException;
import parser.nodes.ArrayNode;
import parser.nodes.Node;
import parser.nodes.ObjectNode;
import parser.nodes.PrimitiveNode;
import token.TokenType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

public class TypeUtils {

    public static boolean isPrimitiveOrPrimitiveWrapperOrString(Class<?> type) {
        return (type.isPrimitive() && type != void.class) || isWrapperType(type) ||
                type == String.class || type == Date.class;
    }

    public static boolean isWrapperType(Class<?> type) {
        return type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class;
    }

    public static boolean isCollectionTypeOrArray(Class<?> type) {
        return type.isArray() || isCollectionFramework(type);
    }

    public static boolean isCollectionFramework(Class<?> type) {
        return isCollection(type);
    }

    private static boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    private static boolean isMap(Class<?> type) {
        return Map.class.isAssignableFrom(type);
    }

    public static boolean isObject(Object object) {
        return !isCollectionTypeOrArray(object.getClass()) && !isPrimitiveOrPrimitiveWrapperOrString(object.getClass());
    }

    public static boolean isObject(Class clazz) {
        return !isCollectionTypeOrArray(clazz) && !isPrimitiveOrPrimitiveWrapperOrString(clazz);
    }

    public static Object createObjectValue(Class<?> classType, Map<String, Node<?>> values) throws JsonParseException {
        try {
            Object targetObject = classType.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, Node<?>> entry :values.entrySet()) {
                Field fieldByName = classType.getField(entry.getKey());

                fieldByName.setAccessible(true);

                Node<?> value = entry.getValue();
                setFieldValue(value, fieldByName, targetObject);
            }

            return targetObject;
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    public static void setFieldValue(Node<?> value, Field fieldByName, Object targetObject) throws JsonParseException {
        try {
            if (value instanceof PrimitiveNode primitiveNode) {
                String val = primitiveNode.getValue();
                Class<?> aClass = fieldByName.getType();
                Object primitiveValue = createPrimitiveValue(primitiveNode.getType(), aClass, val);
                fieldByName.set(targetObject, primitiveValue);
            } else if (value instanceof ArrayNode arrayNode) {
                List<Node<?>> vals = arrayNode.getValue();
                Class<?> aClass = fieldByName.getType();
                Object arrayValue = createCollectionTypeValue(aClass, vals);
                fieldByName.set(targetObject, arrayValue);
            } else if (value instanceof ObjectNode objectNode) {
                Map<String, Node<?>> vals = objectNode.getValue();
                Class<?> aClass = fieldByName.getType();
                Object objectValue = createObjectValue(aClass, vals);
                fieldByName.set(targetObject, objectValue);
            }
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    public static Object createCollectionTypeValue(Class<?> classType, List<Node<?>> values) throws JsonParseException {
        try {
            List<Object> result = new ArrayList<>();

            Class<?> componentType = null;
            if (classType.isArray()) {
                componentType = classType.getComponentType();
            } else if (isCollection(classType)) {
                Type genericSuperclass = classType.getGenericSuperclass();

                if (genericSuperclass instanceof ParameterizedType paramType) {
                    Type[] actualTypeArguments = paramType.getActualTypeArguments();
                    componentType = Class.forName(actualTypeArguments[0].getTypeName());
                }
            }

            for (Node<?> node: values) {
                if (isPrimitiveOrPrimitiveWrapperOrString(componentType)) {
                    Object primitiveValue = createPrimitiveValue(node.getType(), componentType, (String) node.getValue());
                    result.add(primitiveValue);
                } else if (isObject(componentType)) {
                    Object objectValue = createObjectValue(componentType, (Map<String, Node<?>>) node.getValue());
                    result.add(objectValue);
                } else if (isCollectionTypeOrArray(componentType)) {
                    Object collectionTypeValue = createCollectionTypeValue(componentType, (List<Node<?>>) node.getValue());
                    result.add(collectionTypeValue);
                }
            }

            if (classType.isArray()) {
                return result.toArray();
            } else if (isCollection(classType)) {
                Collection<Object> collection = (Collection<Object>) classType.getDeclaredConstructor().newInstance();
                collection.addAll(result);
                return collection;
            } else {
                // todo: add support for Map data type
                return Map.of();
            }
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    public static Object createPrimitiveValue(TokenType tokenType, Class<?> classType, String value) {
        if (tokenType.equals(TokenType.INT) || tokenType.equals(TokenType.FLOAT)) {
            return createNumericalValues(classType, value);
        } else if (tokenType.equals(TokenType.BOOLEAN)) {
            return Boolean.valueOf(value);
        } else {
            return value;
        }
    }

    private static Number createNumericalValues(Class<?> classType, String value) {
        if (Integer.class.equals(classType) || classType.equals(Integer.TYPE)) {
            return Integer.valueOf(value);
        } else if (Long.class.equals(classType) || classType.equals(Long.TYPE)) {
            return Long.valueOf(value);
        } else if (Float.class.equals(classType) || classType.equals(Float.TYPE)) {
            return Float.valueOf(value);
        } else if (Double.class.equals(classType) || classType.equals(Double.TYPE)) {
            return Double.valueOf(value);
        } else if (BigDecimal.class.equals(classType)) {
            return new BigDecimal(value);
        }

        return null;
    }

}
