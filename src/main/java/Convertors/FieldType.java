package Convertors;

public enum FieldType {

    PRIMITIVE(new PrimitiveTypeConvertor()),
    ARRAY(new ArrayTypeConvertor()),
    COLLECTION(new CollectionTypeConvertor());

    private final Convertor convertor;

    FieldType(Convertor convertor) {
        this.convertor = convertor;
    }

    public Convertor getConvertor() {
        return convertor;
    }
}
