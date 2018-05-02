package SQL.Lib.DataHandler;


import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class DataHandler {
     RandomAccessFile randomAccessFile;

     public void close() throws IOException{
             randomAccessFile.close();
     }


}
