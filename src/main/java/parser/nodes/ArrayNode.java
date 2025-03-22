package parser.nodes;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import token.TokenType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ArrayNode implements Node<List<Node<?>>> {

    private List<Node<?>> values;

    @Override
    public List<Node<?>> getValue() {
        return values;
    }

    @Override
    public TokenType getType() {
        return TokenType.ARRAY;
    }

}
