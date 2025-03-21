package token;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
public class Token {

    private TokenType type;
    private String literal;
    private List<Token> child;

    public Token(TokenType type, String literal) {
        this.literal = literal;
        this.type = type;
    }

    public Token(TokenType type, List<Token> child) {
        this.type = type;
        this.child = child;
    }

}
