import Convertors.Convertor;

public class JsonMapper {

    public static String json(Object object) {
        if (object == null) {
            return "null";
        }

        // some other processing
        return Convertor.writeAsString(object);
    }

}
