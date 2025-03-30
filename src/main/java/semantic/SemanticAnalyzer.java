package semantic;


import exception.JsonParseException;
import lexer.Lexer;
import parser.Parser;
import parser.nodes.ArrayNode;
import parser.nodes.Node;
import parser.nodes.ObjectNode;
import parser.nodes.PrimitiveNode;
import token.TokenType;
import util.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class SemanticAnalyzer {

    public <T> T unmarshalJson(String json, Class<T> clazz) throws JsonParseException {
        var parser = new Parser(new Lexer(json));
        Map<String, Node<?>> parsedJson = parser.parseJson();

        return createAndPopulateObject(clazz, parsedJson);
    }

    private <T> T createAndPopulateObject(Class<T> clazz, Map<String, Node<?>> parsedJson) throws JsonParseException {
        try {
            T targetObject = clazz.getDeclaredConstructor().newInstance();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Map.Entry<String, Node<?>> entry :parsedJson.entrySet()) {
                Field fieldByName = getFieldByName(declaredFields, entry.getKey());

                if (fieldByName == null) {
                    throw new JsonParseException(createNonExistentFieldErrorMessage(entry.getKey()), null);
                }
                fieldByName.setAccessible(true);

                Node<?> value = entry.getValue();
                typeMismatch(fieldByName.getName(), fieldByName.getType(), value);

                TypeUtils.setFieldValue(value, fieldByName, targetObject);
            }

            return targetObject;
        } catch (Exception e) {
            throw new JsonParseException("failed to instantiate or populate the target object", e);
        }
    }

    private Field getFieldByName(Field[] fields, String fieldName) {
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        return null;
    }

    private void typeMismatch(String name, Class<?> typeClazz, Node<?> node) throws JsonParseException {
        if (node instanceof PrimitiveNode primitiveNode && TypeUtils.isPrimitiveOrPrimitiveWrapperOrString(typeClazz)) {
            boolean matched = false;
            TokenType tokenType = primitiveNode.getType();

            for (Class<?> cls: tokenType.getAllowedClasses()) {
                if (cls.equals(typeClazz)) {
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                String format = String.format("field %s with type %s did not match the type provided in JSON which is %s", name, typeClazz, tokenType.name());
                throw new JsonParseException(format, null);
            }
        }

        if (node instanceof ArrayNode arrayNode) {
            List<Node<?>> values = arrayNode.getValue();

            if (!TypeUtils.isCollectionTypeOrArray(typeClazz)) {
                String format = String.format("field %s with type %s did not match the type provided in JSON which is %s", name, typeClazz, arrayNode.getType().name());
                throw new JsonParseException(format, null);
            }

            if (typeClazz.isArray()) {
                for (int i=0; i < values.size(); i++) {
                    typeMismatch(name + "[" + i + "]", typeClazz.getComponentType(), values.get(i));
                }
            }

            if (TypeUtils.isCollectionFramework(typeClazz)) {
                Type[] genericInterfaces = typeClazz.getGenericInterfaces();
                if (genericInterfaces[0] instanceof ParameterizedType parameterizedType) {
                    Class<?> componentTypeClass = null;
                    try {
                        componentTypeClass = Class.forName(parameterizedType.getActualTypeArguments()[0].getTypeName());
                    } catch (ClassNotFoundException e) {
                        throw new JsonParseException(e);
                    }
                    for (int i=0; i < values.size(); i++) {
                        typeMismatch(name + "[" + i + "]", componentTypeClass, values.get(i));
                    }
                }
            }
        }

        if (node instanceof ObjectNode objectNode) {
            Map<String, Node<?>> objectNodeValue = objectNode.getValue();

            if (!TypeUtils.isObject(typeClazz)) {
                String format = String.format("field %s with type %s did not match the type provided in JSON which is %s", name, typeClazz, objectNode.getType().name());
                throw new JsonParseException(format, null);
            }

            Field[] declaredFields = typeClazz.getDeclaredFields();
            for (Map.Entry<String, Node<?>> entry :objectNodeValue.entrySet()) {
                Field fieldByName = getFieldByName(declaredFields, entry.getKey());

                if (fieldByName == null) {
                    throw new JsonParseException(createNonExistentFieldErrorMessage(entry.getKey()), null);
                }

                typeMismatch(fieldByName.getName(), fieldByName.getType(), entry.getValue());
            }
        }
    }

    private String createNonExistentFieldErrorMessage(String fieldName) {
        return String.format("filed %s is not present in the provided class", fieldName);
    }

}
