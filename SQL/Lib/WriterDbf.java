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

                for (int i = 0; i < dataDbf.fieldsDbf.length; i++)
                    randomAccessFile.write(dataDbf.fieldsDbf[i].getByteCode(), 0, 32);


            //TODOrandomAccessFile.writeByte(); Тут должен быть терминальный байт,разработчики этого пакета забыли его вставить(
            randomAccessFile.writeByte(13);
            //Записываем записи
                for (int i = 0; i < dataDbf.recordsDbf.length; i++)
                    randomAccessFile.write(dataDbf.recordsDbf[i].getByteCode(), 0, 32);


        }catch (IOException e){
            System.out.println(e);
        }




    }


}
