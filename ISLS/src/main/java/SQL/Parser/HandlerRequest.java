package SQL.Parser;

import SQL.Lib.*;

import java.io.Writer;

public class HandlerRequest {


    protected String[] select(String request){

        return null;
    }
    protected void createTable(String request){//Работа с байтами

    }
    protected void createIndex(String request){//работа с байтами

    }
    protected void insertInto(String request){//можно сделать примитив

    }
    protected void update(String request){//Можно сделать примитив

    }
    protected void delete(String request){//можнос делать примитив

    }
    protected void alterTable(String request){

    }
    protected void truncate(String request){

    }
    protected void dropTable(String request){//рабоат с байтами

    }
    protected void dropIndex(String request){//работа с байтами

    }

    protected void Example(String request){
//        //Если читаем
//        ReaderDbf readerDbf=new ReaderDbf("G:\\bla.dbf");
//        DataDbf dataDbf=readerDbf.read();
//
//        //Работа с отдельными байтами,если вы извращенцы
//        byte [] arr=new byte[32];
//        arr=dataDbf.headerDbf.getByteCode();
//        //И дальше работаешь с массивом,так можно сделать с каждым полем
//        //например
//        arr=dataDbf.fieldsDbf[0].getByteCode();
//        //Для получения всей длины записи
//        int length=dataDbf.recordsDbf[0].getByteCode().length;
//
//
//        //Либо работаете с полями класса через сетеры,это почти эквивалентно массиву байтов
//        dataDbf.headerDbf.setDay((byte)24);
//
//
//        //Для того,чтобы записать вам надо собрать матрешку)) имли если сразу есть Datadbf то можете его сразу записать
//        //Для начала заголовок
//        HeaderDbf headerDbf=new HeaderDbf(arr);
//        FieldDbf[] fieldDbf=new FieldDbf[20];
//
//        fieldDbf[0]=new FieldDbf(arr);
//
//        RecordDbf[] recordDbf=new RecordDbf[20];
//        recordDbf[0]=new RecordDbf((byte)0,200);
//
//        DataDbf dataDbf1=new DataDbf(headerDbf,fieldDbf,recordDbf);
//
//        WriterDbf writerDbf=new WriterDbf("G:\\bla.bdf");
//        writerDbf.write(dataDbf);


    }
}
