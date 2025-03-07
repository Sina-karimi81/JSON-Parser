package convertors;

import data.FieldData;

import java.util.Date;

public class PrimitiveTypeConvertor extends Convertor {

    @Override
    public void marshal(FieldData data, StringBuilder json) {
        if (data.getName() != null) {
            json.append(appendElement(data.getName()))
                    .append(":");
        }
        convert(json, data.getValue());
    }

    private static void convert(StringBuilder json, Object obj) {
        if (obj instanceof String) {
            json.append("\"").append(obj).append("\"");
            return;
        }

        if (obj instanceof Date date) {
            json.append(date.getTime());
            return;
        }

        json.append(obj);
    }

}
