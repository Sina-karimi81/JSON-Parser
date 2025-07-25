package TestObjects.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryResult {

    private List<String> list;
    private Set<Integer> set;
    private int count;

}
