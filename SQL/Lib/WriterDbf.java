package SQL.Lib;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


public class WriterDbf {
    private RandomAccessFile randomAccessFile;
    public WriterDbf(String nameOfFile){
        try {
            randomAccessFile=new RandomAccessFile(nameOfFile,"rw");
        }catch (FileNotFoundException e){
            System.out.println(e);
        }
    }

    public void  write(DataDbf dataDbf){
        //Записываем заголовок файла
        try {
            randomAccessFile.write(dataDbf.headerDbf.getByteCode(), 0, 32);
        }catch (IOException e){
            System.out.println(e);
        }


        //Записываем все поля
        try {
            for (int i = 0; i < dataDbf.fieldsDbf.length; i++) {
                randomAccessFile.write(dataDbf.fieldsDbf[i].getByteCode(),0,32);
            }
        }catch (IOException e){
            System.out.println(e);
        }
        //randomAccessFile.writeByte(); Тут должен быть терминальный байт,разработчики этого пакета забыли его вставить(
        //Записываем записи
        try {
            for (int i = 0; i < dataDbf.recordsDbf.length; i++) {
                randomAccessFile.write(dataDbf.recordsDbf[i].getByteCode(), 0, 32);
            }
        }catch (IOException e){
            System.out.println(e);
        }





    }


}
