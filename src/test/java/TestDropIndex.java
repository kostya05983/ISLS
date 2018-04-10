import SQL.Parser.HandlerRequest;

public class TestDropIndex {

    public static void main(String[] args) {
        String request="DROP INDEX num ON lol;";
        HandlerRequest handlerRequest=new HandlerRequest(null);
        handlerRequest.dropIndex(request);
    }
}
