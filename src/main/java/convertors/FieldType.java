package convertors;

public enum FieldType {

    PRIMITIVE(new PrimitiveTypeConvertor()),
    COLLECTION(new CollectionTypeConvertor()),
    OBJECTS(new ObjectConvertor());

    private final Convertor convertor;

    FieldType(Convertor convertor) {
        this.convertor = convertor;
    }

    public Convertor getConvertor() {
        return convertor;
    }
}
