package SQL.Parser;

class ParserException extends Exception{
    private String message;

    ParserException(String message){
        super(message);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
