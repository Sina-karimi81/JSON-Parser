package exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JsonParseException extends Exception {

    public JsonParseException(String message) {
        super(message);
    }

    public JsonParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonParseException(Throwable cause) {
        super(cause);
    }
}
