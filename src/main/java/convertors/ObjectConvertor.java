package convertors;

import data.FieldData;


public class ObjectConvertor extends Convertor {

    @Override
    public void marshal(FieldData data, StringBuilder json) {
        if (data.getName() != null) {
            json.append(appendElement(data.getName()))
                    .append(":");
        }

        Object value = data.getValue();
        handleObjects(value, json);
    }
}
