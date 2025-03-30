package token;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum TokenType {
    ILLEGAL,
    EOF,
    IDENT,
    INT(int.class, long.class, Integer.class, Long.class, BigDecimal.class),
    FLOAT(float.class, double.class, Float.class, Double.class, BigDecimal.class),
    STRING(String.class),
    COMMA,
    LBRACE,
    RBRACE,
    LBRACKET,
    RBRACKET,
    COLON,
    NULL,
    ARRAY,
    OBJECT,
    BOOLEAN(boolean.class, Boolean.class);

    private final Class[] allowedClasses;

    private TokenType(Class... types) {
        this.allowedClasses = types;
    }
}
