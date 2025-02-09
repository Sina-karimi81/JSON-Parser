import Convertors.Convertor;

public class JsonMapper {

    public static <T> String json(T object) {
        if (object == null) {
            return "null";
        }

        // some other processing
        return Convertor.writeAsString(object);
    }

}
