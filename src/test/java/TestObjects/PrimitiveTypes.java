package TestObjects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrimitiveTypes {

    private int someInt;
    private boolean someBool;
    private float someFloat;

    public PrimitiveTypes(int someInt, boolean someBool, float someFloat) {
        this.someInt = someInt;
        this.someBool = someBool;
        this.someFloat = someFloat;
    }
}
