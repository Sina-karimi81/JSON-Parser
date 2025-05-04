package semantic;


import annotations.ElementType;
import data.TypeMismatchInput;
import exception.JsonParseException;
import lexer.Lexer;
import parser.Parser;
import parser.nodes.ArrayNode;
import parser.nodes.Node;
import parser.nodes.ObjectNode;
import parser.nodes.PrimitiveNode;
import token.TokenType;
import util.TypeUtils;

import java.lang.reflect.*;
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
                typeMismatch(new TypeMismatchInput(fieldByName, fieldByName.getName(), fieldByName.getType(), value));

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

    private void typeMismatch(TypeMismatchInput input) throws JsonParseException {
        if (input.node() instanceof PrimitiveNode primitiveNode && TypeUtils.isPrimitiveOrPrimitiveWrapperOrString(input.clazz())) {
            boolean matched = false;
            TokenType tokenType = primitiveNode.getType();

            for (Class<?> cls: tokenType.getAllowedClasses()) {
                if (cls.equals(input.clazz())) {
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                String format = String.format("field %s with type %s did not match the type provided in JSON which is %s", input.name(), input.clazz(), tokenType.name());
                throw new JsonParseException(format, null);
            }
        }

        if (input.node() instanceof ArrayNode arrayNode) {
            List<Node<?>> values = arrayNode.getValue();

            if (!TypeUtils.isCollectionTypeOrArray(input.clazz())) {
                String format = String.format("field %s with type %s did not match the type provided in JSON which is %s", input.name(), input.clazz(), arrayNode.getType().name());
                throw new JsonParseException(format, null);
            }

            if (input.clazz().isArray()) {
                for (int i=0; i < values.size(); i++) {
                    typeMismatch(new TypeMismatchInput(input.field(), input.name() + "[" + i + "]", input.clazz().getComponentType(), values.get(i)));
                }
            }

            if (TypeUtils.isCollectionFramework(input.clazz())) {
                ElementType annotation = input.field().getAnnotation(ElementType.class);
                if (annotation != null) {
                    for (int i=0; i < values.size(); i++) {
                        typeMismatch(new TypeMismatchInput(input.field(), input.name() + "[" + i + "]", annotation.clazz(), values.get(i)));
                    }
                }
            }
        }

        if (input.node() instanceof ObjectNode objectNode) {
            Map<String, Node<?>> objectNodeValue = objectNode.getValue();

            if (!TypeUtils.isObject(input.clazz())) {
                String format = String.format("field %s with type %s did not match the type provided in JSON which is %s", input.name(), input.clazz(), objectNode.getType().name());
                throw new JsonParseException(format, null);
            }

            Field[] declaredFields = input.clazz().getDeclaredFields();
            for (Map.Entry<String, Node<?>> entry :objectNodeValue.entrySet()) {
                Field fieldByName = getFieldByName(declaredFields, entry.getKey());

                if (fieldByName == null) {
                    throw new JsonParseException(createNonExistentFieldErrorMessage(entry.getKey()), null);
                }

                typeMismatch(new TypeMismatchInput(null, fieldByName.getName(), fieldByName.getType(), entry.getValue()));
            }
        }
    }

    private String createNonExistentFieldErrorMessage(String fieldName) {
        return String.format("filed %s is not present in the provided class", fieldName);
    }

}
