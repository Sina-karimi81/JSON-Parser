import Convertors.Convertor;

public class JsonMapper {

    public static <T> String json(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Cannot convert a null input parameter");
        }

        StringBuilder json = new StringBuilder();
        json.append("{").append("\n");

        // some other processing
        String convertedResult = Convertor.writeAsString(object);
        json.append(convertedResult);

        json.append("}");
        return json.toString();
    }

}
