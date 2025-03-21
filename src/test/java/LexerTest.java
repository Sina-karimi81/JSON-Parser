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
    public void testOffByOneWhenNumberIsInvolved() {
        String json = "{\"grade\": 65.456, \"isStudent\": false}";

        Lexer lexer = new Lexer(json);
        List<Token> actualTokens = new ArrayList<>();

        Token token;
        while ((token = lexer.nextToken()).getType() != TokenType.EOF) {
            actualTokens.add(token);
        }

        // Expected tokens
        List<Token> expectedTokens = List.of(
                new Token(TokenType.LBRACE, "{"),
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

        List<Token> childTokens = List.of(
                new Token(TokenType.IDENT, "grade"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.FLOAT, "65.456"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "isStudent"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.BOOLEAN, "false")
        );

        // Expected tokens
        List<Token> expectedTokens = List.of(
                new Token(TokenType.LBRACE, "{"),
                new Token(TokenType.IDENT, "name"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.STRING, "John"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "data"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.OBJECT, childTokens),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "address"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.NULL, "null"),
                new Token(TokenType.RBRACE, "}")
        );

        assertEquals(expectedTokens, actualTokens, "Lexer should generate correct tokens");
    }

    @Test
    public void testArrayInObjectLexerOutput() {
        String json = "{\"name\": \"John\", \"data\": {\"grade\": 65.456, \"isStudent\": false," +
                " \"courses\": [\"AP\",\"Cloud\",\"Compiler\"]}, \"address\": null}";

        Lexer lexer = new Lexer(json);
        List<Token> actualTokens = new ArrayList<>();

        Token token;
        while ((token = lexer.nextToken()).getType() != TokenType.EOF) {
            actualTokens.add(token);
        }

        List<Token> childChildTokens = List.of(
                new Token(TokenType.STRING, "AP"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.STRING, "Cloud"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.STRING, "Compiler")
        );

        List<Token> childTokens = List.of(
                new Token(TokenType.IDENT, "grade"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.FLOAT, "65.456"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "isStudent"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.BOOLEAN, "false"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "courses"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.ARRAY, childChildTokens)
        );

        // Expected tokens
        List<Token> expectedTokens = List.of(
                new Token(TokenType.LBRACE, "{"),
                new Token(TokenType.IDENT, "name"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.STRING, "John"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "data"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.OBJECT, childTokens),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "address"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.NULL, "null"),
                new Token(TokenType.RBRACE, "}")
        );

        assertEquals(expectedTokens, actualTokens, "Lexer should generate correct tokens");
    }

    @Test
    public void testObjectInArrayLexerOutput() {
        String json = "{\"name\": \"John\"," +
                " \"courses\": [{\"name\": \"AP\", \"points\": 3}, {\"name\": \"Cloud\", \"points\": 3}, {\"name\": \"Compiler\", \"points\": 3}], \"address\": null}";

        Lexer lexer = new Lexer(json);
        List<Token> actualTokens = new ArrayList<>();

        Token token;
        while ((token = lexer.nextToken()).getType() != TokenType.EOF) {
            actualTokens.add(token);
        }

        List<Token> courseOne = List.of(
                new Token(TokenType.IDENT, "name"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.STRING, "AP"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "points"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.INT, "3")
        );

        List<Token> courseTwo = List.of(
                new Token(TokenType.IDENT, "name"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.STRING, "Cloud"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "points"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.INT, "3")
        );

        List<Token> courseThree = List.of(
                new Token(TokenType.IDENT, "name"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.STRING, "Compiler"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "points"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.INT, "3")
        );

        List<Token> childTokens = List.of(
                new Token(TokenType.OBJECT, courseOne),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.OBJECT, courseTwo),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.OBJECT, courseThree)
        );

        // Expected tokens
        List<Token> expectedTokens = List.of(
                new Token(TokenType.LBRACE, "{"),
                new Token(TokenType.IDENT, "name"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.STRING, "John"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "courses"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.ARRAY, childTokens),
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

    @Test
    public void testIllegalLexerOutput() {
        String json = "{\"name\": \"John\", \"admissionDate\": +, \"isStudent\": false}";

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
                new Token(TokenType.ILLEGAL, "+"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "isStudent"),
                new Token(TokenType.COLON, ":"),
                new Token(TokenType.BOOLEAN, "false"),
                new Token(TokenType.RBRACE, "}")
        );

        assertEquals(expectedTokens, actualTokens, "Lexer should generate correct tokens");
    }
}
