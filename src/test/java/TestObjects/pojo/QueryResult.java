package TestObjects.pojo;

import annotations.ElementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryResult {

    @ElementType(clazz = String.class)
    private List<String> list;
    @ElementType(clazz = Integer.class)
    private Set<Integer> set;
    private int count;

}
