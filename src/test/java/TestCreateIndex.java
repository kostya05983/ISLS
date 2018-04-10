
import SQL.Lib.DataHandler.ReaderDbf;
import SQL.Lib.Indexes.DataIdx;


public class TestCreateIndex {

//    public static void main(String[] args) throws Exception {
//        HandlerRequest handlerRequest=new HandlerRequest(null);
//        handlerRequest.createIndex("");
//  }

    public static void main(String[] args) {
        DataIdx dataIdx;
        ReaderDbf readerDbf=new ReaderDbf("num.idx");
        dataIdx=readerDbf.readIdx();

    }
}
