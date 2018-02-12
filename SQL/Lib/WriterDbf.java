package SQL.Lib;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class WriterDbf {
   FileOutputStream fileOutputStream;
    public WriterDbf(String nameOfFile){
        try {
            fileOutputStream=new FileOutputStream(nameOfFile);
        }catch (FileNotFoundException e){
            System.out.println(e);
        }
    }

    protected void  write(DataDbf dataDbf){

    //fileOutputStream.write();

    }


}
