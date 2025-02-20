package TestObjects.ultimate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestObject {

    private QueryResult queryResult;
    private Product product;
    private Date date;

}
