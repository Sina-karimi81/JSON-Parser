package data;

import lombok.Getter;
import parser.nodes.Node;

import java.lang.reflect.Field;

public record TypeMismatchInput(Field field, String name, Class<?> clazz, Node<?> node) {
}
