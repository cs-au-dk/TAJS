package dk.brics.tajs.util;

/**
 * Exception for signaling syntactically invalid source files.
 */
public class ParseError extends RuntimeException {

    /**
     * Constructs a new exception.
     */
    public ParseError(String msg) {
        super(msg);
    }
}
