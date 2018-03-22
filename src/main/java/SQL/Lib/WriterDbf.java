package SQL.Lib;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


public class WriterDbf {

    private RandomAccessFile randomAccessFile;

    public WriterDbf(String nameOfFile){
        File file=new File(nameOfFile);
        file.delete();
        try {
            randomAccessFile=new RandomAccessFile(nameOfFile,"rw");
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void  write(DataDbf dataDbf){


        try {
            int sumOfBytes=0;
            //Записываем заголовок файла

                randomAccessFile.write(dataDbf.headerDbf.getByteCode(), 0, 32);

            //Записываем все поля

                for (int i = 0; i < dataDbf.fieldsDbf.size(); i++) {
                    sumOfBytes+=dataDbf.fieldsDbf.get(i).getSizeField();
                    randomAccessFile.write(dataDbf.fieldsDbf.get(i).getByteCode(), 0, 32);
                }
                //Терминальный байт
            randomAccessFile.writeByte(13);

            sumOfBytes++;

            int buf=sumOfBytes;
            //Записываем записи
            if(dataDbf.recordsDbf!=null)
                for (int i = 0; i < dataDbf.recordsDbf.size(); i++) {
                    buf=sumOfBytes;
                    if(dataDbf.recordsDbf.get(i).getByteCode().length==sumOfBytes)
                    randomAccessFile.write(dataDbf.recordsDbf.get(i).getByteCode(), 0, dataDbf.recordsDbf.get(i).getByteCode().length);
                    else
                    {
                        randomAccessFile.write(dataDbf.recordsDbf.get(i).getByteCode(), 0, dataDbf.recordsDbf.get(i).getByteCode().length);
                        buf-=dataDbf.recordsDbf.get(i).getByteCode().length;
                        for(int j=0;j<buf;j++){
                            randomAccessFile.write(0);
                        }
                    }
                }

            randomAccessFile.writeByte(12);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public void close(){
        try {
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
