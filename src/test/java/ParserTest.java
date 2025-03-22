import lexer.Lexer;
import org.junit.jupiter.api.Test;
import parser.Parser;
import parser.nodes.ArrayNode;
import parser.nodes.Node;
import parser.nodes.ObjectNode;
import parser.nodes.PrimitiveNode;
import token.TokenType;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {

    @Test
    public void testSimpleParserOutput() {
        String json = "{\"name\": \"John\", \"age\": 30, \"isStudent\": false, \"grade\": 65.456}";

        Lexer lexer = new Lexer(json);
        Parser parser = new Parser(lexer);

        Map<String, Node<?>> result = parser.parseJson();

        // Expected tokens
        Map<String, Node<?>> expectedTokens = Map.of(
                "name", new PrimitiveNode(TokenType.STRING, "John"),
                "age", new PrimitiveNode(TokenType.INT, "30"),
                "isStudent", new PrimitiveNode(TokenType.BOOLEAN, "false"),
                "grade", new PrimitiveNode(TokenType.FLOAT, "65.456")
        );

        assertEquals(expectedTokens, result, "Parser should generate correct nodes");
    }

    @Test
    public void testObjectAndNullParserOutput() {
        String json = "{\"name\": \"John\", \"data\": {\"grade\": 65.456, \"isStudent\": false}, \"address\": null}";

        Lexer lexer = new Lexer(json);
        Parser parser = new Parser(lexer);

        Map<String, Node<?>> result = parser.parseJson();

        Map<String, Node<?>> innerMap = Map.of(
                "grade", new PrimitiveNode(TokenType.FLOAT, "65.456"),
                "isStudent", new PrimitiveNode(TokenType.BOOLEAN, "false")
        );

        // Expected tokens
        Map<String, Node<?>> expectedTokens = Map.of(
                "name", new PrimitiveNode(TokenType.STRING, "John"),
                "data", new ObjectNode(innerMap),
                "address", new PrimitiveNode(TokenType.NULL, "null")
        );

        assertEquals(expectedTokens, result, "Parser should generate correct nodes");
    }

    @Test
    public void testArrayInObjectParserOutput() {
        String json = "{\"name\": \"John\", \"data\": {\"grade\": 65.456, \"isStudent\": false," +
                " \"courses\": [\"AP\",\"Cloud\",\"Compiler\"]}, \"address\": null}";

        Lexer lexer = new Lexer(json);
        Parser parser = new Parser(lexer);

        Map<String, Node<?>> result = parser.parseJson();

        List<Node<?>> childChildNodes = List.of(
                new PrimitiveNode(TokenType.STRING, "AP"),
                new PrimitiveNode(TokenType.STRING, "Cloud"),
                new PrimitiveNode(TokenType.STRING, "Compiler")
        );



        Map<String, Node<?>> innerMap = Map.of(
                "grade", new PrimitiveNode(TokenType.FLOAT, "65.456"),
                "isStudent", new PrimitiveNode(TokenType.BOOLEAN, "false"),
                "courses", new ArrayNode(childChildNodes)
        );

        // Expected tokens
        Map<String, Node<?>> expectedTokens = Map.of(
                "name", new PrimitiveNode(TokenType.STRING, "John"),
                "data", new ObjectNode(innerMap),
                "address", new PrimitiveNode(TokenType.NULL, "null")
        );

        assertEquals(expectedTokens, result, "Parser should generate correct nodes");
    }

    @Test
    public void testObjectInArrayParserOutput() {
        String json = "{\"name\": \"John\"," +
                " \"courses\": [{\"name\": \"AP\", \"points\": 3}, {\"name\": \"Cloud\", \"points\": 3}, {\"name\": \"Compiler\", \"points\": 3}], \"address\": null}";

        Lexer lexer = new Lexer(json);
        Parser parser = new Parser(lexer);

        Map<String, Node<?>> result = parser.parseJson();

        Map<String, Node<?>> courseOne = Map.of(
                "name", new PrimitiveNode(TokenType.STRING, "AP"),
                "points", new PrimitiveNode(TokenType.INT, "3")
        );

        Map<String, Node<?>> courseTwo = Map.of(
                "name", new PrimitiveNode(TokenType.STRING, "Cloud"),
                "points", new PrimitiveNode(TokenType.INT, "3")
        );

        Map<String, Node<?>> courseThree = Map.of(
                "name", new PrimitiveNode(TokenType.STRING, "Compiler"),
                "points", new PrimitiveNode(TokenType.INT, "3")
        );

        List<Node<?>> innerList = List.of(
                new ObjectNode(courseOne),
                new ObjectNode(courseTwo),
                new ObjectNode(courseThree)
        );

        // Expected tokens
        Map<String, Node<?>> expectedTokens = Map.of(
                "name", new PrimitiveNode(TokenType.STRING, "John"),
                "courses", new ArrayNode(innerList),
                "address", new PrimitiveNode(TokenType.NULL, "null")
        );

        assertEquals(expectedTokens, result, "Parser should generate correct nodes");
    }

    @Test
    public void testIllegalParserOutput() {
        String json = "{\"name\": \"John\", \"admissionDate\": +, \"isStudent\": false}";

        Lexer lexer = new Lexer(json);
        Parser parser = new Parser(lexer);

        parser.parseJson();

        List<String> errors = parser.getErrors();

        assertEquals("illegal token found in input: + for identifier: admissionDate", errors.get(0), "Parser should generate correct nodes");
    }

}
