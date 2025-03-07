package token;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    private TokenType type;
    private String literal;

}
