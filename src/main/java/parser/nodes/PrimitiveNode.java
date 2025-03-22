package parser.nodes;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import token.TokenType;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PrimitiveNode implements Node<String> {

    private TokenType type;
    private String value;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public TokenType getType() {
        return type;
    }
}
