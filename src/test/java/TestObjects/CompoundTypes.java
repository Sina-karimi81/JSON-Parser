package TestObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompoundTypes {

    private int[] someInts;
    private boolean[] someBools;
    private float[] someFloats;
    private int someInt;
    private boolean someBool;
    private float someFloat;

}
