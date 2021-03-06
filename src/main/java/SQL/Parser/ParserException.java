package SQL.Parser;

public class ParserException extends Exception {
    private final String message;

    public ParserException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
