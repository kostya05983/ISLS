package SQL.Lib;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ReaderDbf {

    RandomAccessFile randomAccessFile;

    public ReaderDbf(String nameOfFile){
        try{
            randomAccessFile=new RandomAccessFile(nameOfFile,"r");
        }catch(FileNotFoundException e){
            System.out.println(e);
        }
    }
    public DataDbf read(){
        byte[] buf=new byte[32];

        try {
            randomAccessFile.read(buf);
        }catch (IOException e){
            System.out.println(e);
        }
        HeaderDbf headerDbf=new HeaderDbf(buf);

        //Читаем поля пока не встретим терминальный знак

        return null;
    }

}
