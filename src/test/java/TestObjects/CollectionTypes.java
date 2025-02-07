package TestObjects;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CollectionTypes {

    private List<String> someList;
    private Map<String, Integer> someMap;
    private Set<Integer> someSet;
    private Queue<Integer> someQueue;

}
