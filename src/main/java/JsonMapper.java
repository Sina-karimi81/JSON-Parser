import convertors.Convertor;
import exception.JsonParseException;
import semantic.SemanticAnalyzer;

public class JsonMapper {

    private static final SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

    public static String json(Object object) {
        if (object == null) {
            return "null";
        }

        return Convertor.writeAsString(object);
    }

    public static <T> T object(String json, Class<T> clazz) throws JsonParseException {
        if (clazz == null) {
            throw new IllegalArgumentException("class type cannot be null");
        }

        if (json.equals("{}") || json.equals("null")) {
            return null;
        }

        return semanticAnalyzer.unmarshalJson(json, clazz);
    }
}
