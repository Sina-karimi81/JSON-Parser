package lexer;

import token.Token;
import token.TokenType;

import java.util.Map;

public class Lexer {

    private static final Map<String, TokenType> KEYWORDS = Map.of(
            "true", TokenType.BOOLEAN,
            "false", TokenType.BOOLEAN,
            "null", TokenType.NULL
    );

    private final String input;
    private int currentPosition;
    private int readPosition;
    private char currentChar;

    public Lexer(String input) {
        this.input = input;
        this.readChar();
    }

    public Token nextToken() {
        Token result;

        this.skipWhiteSpace();

        switch (this.currentChar) {
            case ',' -> result = createToken(TokenType.COMMA, String.valueOf(this.currentChar));
            case '{' -> result = createToken(TokenType.LBRACE, String.valueOf(this.currentChar));
            case '}' -> result = createToken(TokenType.RBRACE, String.valueOf(this.currentChar));
            case '[' -> result = createToken(TokenType.LBRACKET, String.valueOf(this.currentChar));
            case ']' -> result = createToken(TokenType.RBRACKET, String.valueOf(this.currentChar));
            case ':' -> result = createToken(TokenType.COLON, String.valueOf(this.currentChar));
            case '\"' -> {
                this.readChar();
                String literal = this.readStringLiteral();

                if (this.peekChar() == ':') {
                    result = createToken(TokenType.IDENT, literal);
                } else {
                    result = createToken(TokenType.STRING, literal);
                }
            }
            case 0 -> result = createToken(TokenType.EOF, "");
            default -> {
                if (isLetter(this.currentChar)) {
                    char firstLetter = this.currentChar;
                    this.readChar();

                    String literal = firstLetter + this.readIdentifier();
                    TokenType type = KEYWORDS.get(literal.toLowerCase());
                    return createToken(type, literal);
                } else if (isDigit(this.currentChar)) {
                    String main = this.readNumber();

                    if (this.currentChar == '.') {
                        this.readChar();
                        String decimalValue = this.readNumber();

                        return createToken(TokenType.FLOAT, main + '.' + decimalValue);
                    } else {
                        return createToken(TokenType.INT, main);
                    }
                } else {
                    return createToken(TokenType.ILLEGAL, String.valueOf(this.currentChar));
                }
            }
        }

        this.readChar();
        return result;
    }

    private void readChar() {
        if (this.readPosition >= input.length()) {
            this.currentChar = 0; // NUL
        } else {
            this.currentChar = input.charAt(this.readPosition);
        }

        this.currentPosition = this.readPosition;
        this.readPosition++;
    }

    private char peekChar() {
        if (this.readPosition >= this.input.length()) {
            return 0;
        } else {
            return input.charAt(this.readPosition);
        }
    }

    private Token createToken(TokenType type, String ch) {
        return  new Token(type, ch);
    }

    private String readIdentifier() {
        int tempPosition = this.currentPosition;
        while (isLetter(this.currentChar)) {
            this.readChar();
        }

        return this.input.substring(tempPosition, this.currentPosition);
    }

    private String readStringLiteral() {
        int tempPosition = this.currentPosition;
        while (this.currentChar != '\"') {
            this.readChar();
        }

        return this.input.substring(tempPosition, this.currentPosition);
    }

    private String readNumber() {
        int tempPosition = this.currentPosition;
        while (isDigit(this.currentChar)) {
            this.readChar();
        }

        return this.input.substring(tempPosition, this.currentPosition);
    }

    private void skipWhiteSpace() {
        while (this.currentChar == ' ' || this.currentChar == '\n' || this.currentChar == '\t' || this.currentChar == '\r') {
            this.readChar();
        }
    }

    private boolean isLetter(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
    }

    private boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }
}
