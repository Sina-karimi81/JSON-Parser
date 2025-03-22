package parser.nodes;

import lombok.*;
import token.TokenType;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ObjectNode implements Node<Map<String, Node<?>>> {

    private Map<String, Node<?>> values;

    @Override
    public Map<String, Node<?>> getValue() {
        return values;
    }

    @Override
    public TokenType getType() {
        return TokenType.OBJECT;
    }
}
