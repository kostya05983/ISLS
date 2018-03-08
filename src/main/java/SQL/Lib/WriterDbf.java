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
        try {
            //Записываем заголовок файла

                randomAccessFile.write(dataDbf.headerDbf.getByteCode(), 0, 32);

            //Записываем все поля

                for (int i = 0; i < dataDbf.fieldsDbf.size(); i++)
                    randomAccessFile.write(dataDbf.fieldsDbf.get(i).getByteCode(), 0, 32);

                //Терминальный байт
            randomAccessFile.writeByte(13);
            //Записываем записи
                for (int i = 0; i < dataDbf.recordsDbf.size(); i++)
                    randomAccessFile.write(dataDbf.recordsDbf.get(i).getByteCode(), 0, dataDbf.recordsDbf.get(i).getByteCode().length);

            randomAccessFile.writeByte(12);
        }catch (IOException e){
            System.out.println(e);
        }

    }


}
