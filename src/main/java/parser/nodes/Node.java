package parser.nodes;

import token.TokenType;

public interface Node<T> {

    T getValue();
    TokenType getType();

}
