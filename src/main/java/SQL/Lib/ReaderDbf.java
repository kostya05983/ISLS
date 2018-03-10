package SQL.Lib;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

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
        long position=32;
        try {
            randomAccessFile.read(buf);

            HeaderDbf headerDbf = new HeaderDbf(buf);

            //Читаем поля пока не встретим терминальный знак
            ArrayList<FieldDbf> fieldsDbf=new ArrayList<>();
            FieldDbf fieldDbf;
            int sizeOfRecord=0;

            while (randomAccessFile.readByte()!=13) {
                randomAccessFile.seek(position);
                randomAccessFile.read(buf);
                System.out.println(randomAccessFile.getChannel().position());
                fieldDbf=new FieldDbf(buf);
                fieldsDbf.add(fieldDbf);
                sizeOfRecord+=buf[16];//Находим длину записи
                position+=32;
            }
            position++;
            sizeOfRecord++;

            byte[] bufRecord=new byte[sizeOfRecord];
            RecordDbf recordDbf;
            ArrayList<RecordDbf> recordsDbf=new ArrayList<>();
            while(randomAccessFile.readByte()!=12){
                randomAccessFile.seek(position);
                randomAccessFile.read(bufRecord);
                recordDbf=new RecordDbf(bufRecord);
                recordsDbf.add(recordDbf);
                position+=sizeOfRecord;
            }
            DataDbf datadbf=new DataDbf(headerDbf,fieldsDbf,recordsDbf);
            return datadbf;

        }catch (IOException e){
            e.printStackTrace();
            return null;
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
