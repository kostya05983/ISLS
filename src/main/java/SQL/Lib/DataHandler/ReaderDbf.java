package SQL.Lib.DataHandler;

import SQL.Lib.Dbf.DataDbf;
import SQL.Lib.Dbf.FieldDbf;
import SQL.Lib.Dbf.HeaderDbf;
import SQL.Lib.Dbf.RecordDbf;
import SQL.Lib.Indexes.BTreeIdx;
import SQL.Lib.Indexes.DataIdx;
import SQL.Lib.Indexes.HeaderIdx;
import javafx.scene.control.Alert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ReaderDbf extends DataHandler{

    public ReaderDbf(String nameOfFile){
        try{
            randomAccessFile=new RandomAccessFile(nameOfFile,"r");
        }catch(FileNotFoundException e){
            out_stack_error(e.getLocalizedMessage(), e.getMessage());
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
//                out_stack_error("RandomAccessFile error on pos:",Long.toString(randomAccessFile.getChannel().position()));
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
            return new DataDbf(headerDbf,fieldsDbf,recordsDbf);

        }catch (IOException e){
            out_stack_error(e.getLocalizedMessage(), e.getMessage());
            return null;
        }
    }

    public DataIdx readIdx(){
        HeaderIdx headerIdx=new HeaderIdx();
        BTreeIdx bTreeIdx=new BTreeIdx();
        byte[] buffer=new byte[512];
        try {
            randomAccessFile.read(buffer);
            headerIdx.setByteCode(buffer);
            buffer=new byte[headerIdx.getEndPointer()-512];

            randomAccessFile.read(buffer);
            bTreeIdx.setByteCode(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DataIdx(headerIdx,bTreeIdx);
    }

    public void close(){
        try {
            randomAccessFile.close();
        } catch (IOException e) {
            //e.printStackTrace();
            out_stack_error(e.getLocalizedMessage(), e.getMessage());
        }
    }
}
