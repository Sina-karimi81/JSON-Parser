package parser.nodes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import token.Token;
import token.TokenType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IllegalNode implements Node<String> {

    private Token token;

    @Override
    public String getValue() {
        return "Illegal Token";
    }

    @Override
    public TokenType getType() {
        return token.getType();
    }
}
