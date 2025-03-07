import lexer.Lexer;
import org.junit.jupiter.api.Test;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    @Test
    public void testSimpleLexerOutput() {
        String json = "{\"name\": \"John\", \"age\": 30, \"isStudent\": false}";

        Lexer lexer = new Lexer(json);
        List<Token> actualTokens = new ArrayList<>();

        Token token;
        while ((token = lexer.nextToken()).getType() != TokenType.EOF) {
            actualTokens.add(token);
        }

        // Expected tokens
        List<Token> expectedTokens = List.of(
                new Token(TokenType.LBRACE, "{"),
                new Token(TokenType.IDENT, "name"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.STRING, "John"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "age"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.INT, "30"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "isStudent"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.BOOLEAN, "false"),
                new Token(TokenType.RBRACE, "}")
        );

        assertEquals(expectedTokens, actualTokens, "Lexer should generate correct tokens");
    }

    @Test
    public void testFloatLexerOutput() {
        String json = "{\"name\": \"John\", \"grade\": 65.456, \"isStudent\": false}";

        Lexer lexer = new Lexer(json);
        List<Token> actualTokens = new ArrayList<>();

        Token token;
        while ((token = lexer.nextToken()).getType() != TokenType.EOF) {
            actualTokens.add(token);
        }

        // Expected tokens
        List<Token> expectedTokens = List.of(
                new Token(TokenType.LBRACE, "{"),
                new Token(TokenType.IDENT, "name"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.STRING, "John"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "grade"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.FLOAT, "65.456"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "isStudent"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.BOOLEAN, "false"),
                new Token(TokenType.RBRACE, "}")
        );

        assertEquals(expectedTokens, actualTokens, "Lexer should generate correct tokens");
    }

    @Test
    public void testObjectAndNullLexerOutput() {
        String json = "{\"name\": \"John\", \"data\": {\"grade\": 65.456, \"isStudent\": false}, \"address\": null}";

        Lexer lexer = new Lexer(json);
        List<Token> actualTokens = new ArrayList<>();

        Token token;
        while ((token = lexer.nextToken()).getType() != TokenType.EOF) {
            actualTokens.add(token);
        }

        // Expected tokens
        List<Token> expectedTokens = List.of(
                new Token(TokenType.LBRACE, "{"),
                new Token(TokenType.IDENT, "name"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.STRING, "John"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "data"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.LBRACE, "{"),
                new Token(TokenType.IDENT, "grade"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.FLOAT, "65.456"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "isStudent"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.BOOLEAN, "false"),
                new Token(TokenType.RBRACE, "}"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "address"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.NULL, "null"),
                new Token(TokenType.RBRACE, "}")
        );

        assertEquals(expectedTokens, actualTokens, "Lexer should generate correct tokens");
    }

    @Test
    public void testDateLexerOutput() {
        String json = "{\"name\": \"John\", \"admissionDate\": \"2024-05-16\", \"isStudent\": false}";

        Lexer lexer = new Lexer(json);
        List<Token> actualTokens = new ArrayList<>();

        Token token;
        while ((token = lexer.nextToken()).getType() != TokenType.EOF) {
            actualTokens.add(token);
        }

        // Expected tokens
        List<Token> expectedTokens = List.of(
                new Token(TokenType.LBRACE, "{"),
                new Token(TokenType.IDENT, "name"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.STRING, "John"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "admissionDate"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.STRING, "2024-05-16"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "isStudent"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.BOOLEAN, "false"),
                new Token(TokenType.RBRACE, "}")
        );

        assertEquals(expectedTokens, actualTokens, "Lexer should generate correct tokens");
    }
}
