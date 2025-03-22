package parser;

import lexer.Lexer;
import lombok.Getter;
import parser.nodes.*;
import token.Token;
import token.TokenType;

import java.util.*;

public class Parser {

    private Lexer lexer;
    private Token currentToken;
    private Token peekToken;
    @Getter
    private List<String> errors;

    public Parser(Lexer lexer) {
        this.errors = new ArrayList<>();
        this.lexer = lexer;

        nextToken();
        nextToken();
    }

    public Map<String, Node<?>> parseJson() {
        Map<String, Node<?>> result = new HashMap<>();

        while (!currTokenIs(TokenType.EOF)) {
            String fieldName = null;
            if (currTokenIs(TokenType.IDENT)) {
                fieldName = currentToken.getLiteral();

                if (expectedPeek(TokenType.COLON)) {
                    nextToken();
                    Node<?> value = createNode(currentToken);
                    if (value != null) {
                        result.put(fieldName, value);
                    } else {
                        if (currTokenIs(TokenType.ILLEGAL)) {
                            errors.add("illegal token found in input: " + currentToken.getLiteral() + " for identifier: " + fieldName);
                        }
                    }
                }
            } else if (currTokenIs(TokenType.ILLEGAL)) {
                errors.add("illegal token found in input: " + currentToken.getLiteral());
            }

            nextToken();
        }

        return result;
    }

    private Node<?> createNode(Token inputToken) {
        return switch (inputToken.getType()) {
            case STRING, FLOAT, INT, BOOLEAN, NULL ->
                    new PrimitiveNode(inputToken.getType(), inputToken.getLiteral());
            case OBJECT -> new ObjectNode(parseObjects(inputToken.getChild()));
            case ARRAY -> new ArrayNode(parseArrays(inputToken.getChild()));
            default -> null;
        };
    }

    private Map<String, Node<?>> parseObjects(List<Token> tokens) {
        Map<String, Node<?>> result = new HashMap<>();

        ListIterator<Token> tokenListIterator = tokens.listIterator();
        while (tokenListIterator.hasNext()) {
            Token next = tokenListIterator.next();
            String fieldName;
            if (next.getType().equals(TokenType.IDENT)) {
                fieldName = next.getLiteral();

                Token colonToken = tokenListIterator.next();
                if (colonToken.getType().equals(TokenType.COLON)) {
                    Token value = tokenListIterator.next();
                    Node<?> node = createNode(value);
                    if (node != null) {
                        result.put(fieldName, node);
                    } else {
                        if (currTokenIs(TokenType.ILLEGAL)) {
                            errors.add("illegal token found in input: " + currentToken.getLiteral() + " for identifier: " + fieldName);
                        }
                    }
                } else {
                    peekError(TokenType.COLON);
                }
            } else if (currTokenIs(TokenType.ILLEGAL)) {
                errors.add("illegal token found in input: " + currentToken.getLiteral());
            }
        }

        return result;
    }

    private List<Node<?>> parseArrays(List<Token> tokens) {
        List<Node<?>> result = new ArrayList<>();

        for (Token next : tokens) {
            if (!next.getType().equals(TokenType.COMMA)) {
                Node<?> node = createNode(next);
                if (node != null) {
                    result.add(node);
                } else {
                    if (currTokenIs(TokenType.ILLEGAL)) {
                        errors.add("illegal token found in input: " + currentToken.getLiteral());
                    }
                }
            } else if (currTokenIs(TokenType.ILLEGAL)) {
                errors.add("illegal token found in input: " + currentToken.getLiteral());
            }
        }

        return result;
    }

    private void nextToken() {
        currentToken = peekToken;
        peekToken = lexer.nextToken();
    }

    private void peekError(TokenType tokenType) {
        String format = String.format("expected next token to be %s, got %s instead", tokenType, peekToken.getType());
        errors.add(format);
    }

    private boolean currTokenIs(TokenType tokenType) {
        return currentToken.getType().equals(tokenType);
    }

    private boolean peekTokenIs(TokenType tokenType) {
        return peekToken.getType().equals(tokenType);
    }

    private boolean expectedPeek(TokenType tokenType) {
        if (peekTokenIs(tokenType)) {
            nextToken();
            return true;
        } else {
            peekError(tokenType);
            return false;
        }
    }

}
