package SQL.Parser;

import SQL.Lib.*;


import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HandlerRequest {


    protected String[] select(String request){

        return null;
    }

    public void createTable(String request){//Работа с байтами
        request=request.substring(request.indexOf(" ")+1);
        request=request.substring(request.indexOf(" ")+1);
        String tableName=request.substring(0,request.indexOf("(")).trim();
        request=request.substring(request.indexOf("(")+1);

        ArrayList<String> fieldsNames=new ArrayList<>();
        ArrayList<TypesOfFields> types=new ArrayList<>();
        ArrayList<Byte> sizes=new ArrayList<>();

        String type;
        request=request.trim();
        byte size=0;

        while(!request.equals(");")) {
            size=0;
            fieldsNames.add( request.substring(0,request.indexOf(" ")));
            request=request.substring(request.indexOf(" ")).trim();

            type=request.substring(0,request.indexOf(")")+2);
            request=request.substring(request.indexOf(")")+2);
                if(type.indexOf(",")>=0&&type.indexOf(",")!=type.length()-1){
                    System.out.println(type.substring(type.indexOf("("),type.indexOf(",")));
                    size+=Short.parseShort(type.substring(type.indexOf("(")+1,type.indexOf(",")));
                    size++;
                    size+=Short.parseShort(type.substring(type.indexOf(",")+1,type.indexOf(")")));
                    type=type.substring(0,type.indexOf("("));
                }else {
                    size += Short.parseShort(type.substring(type.indexOf("(") + 1, type.indexOf(")")));
                    type = type.substring(0, type.indexOf("("));
                }

            sizes.add(size);
            if(type.toLowerCase().equals("char"))
                types.add(TypesOfFields.Character);
            if(type.toLowerCase().equals("integer")) {
                types.add(TypesOfFields.Integer);

            }
            if(type.toLowerCase().equals("float")) {
                types.add(TypesOfFields.Float);

            }
            request=request.trim();
        }

        short sum=0;

        for(int i=0;i<sizes.size();i++)
            sum+=sizes.get(i);

        GregorianCalendar gregorianCalendar=new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(System.currentTimeMillis());

        HeaderDbf headerDbf=new HeaderDbf();

        headerDbf.setSignature((byte)5);//Номер версии

        headerDbf.setYear((byte)(gregorianCalendar.get(Calendar.YEAR)%1000));
        headerDbf.setMonth((byte)gregorianCalendar.get(Calendar.MONTH));
        headerDbf.setDay((byte)gregorianCalendar.get(Calendar.DAY_OF_MONTH));
        headerDbf.setNumberOfRecords(0);
        headerDbf.setLengthOfTitle((short)(32*fieldsNames.size()));
        headerDbf.setLengthOfRecord(sum);
        headerDbf.setFlagTransaction((byte)0);
        headerDbf.setFlagEncryption((byte)0);

        FieldDbf fieldDbf;
        ArrayList<FieldDbf> fieldDbfs=new ArrayList<>();
        for(int i=0;i<fieldsNames.size();i++) {
            fieldDbf = new FieldDbf();
            fieldDbf.setNameField(fieldsNames.get(i));
            fieldDbf.setTypeField(types.get(i).code);
            fieldDbf.setSizeField(sizes.get(i));
            fieldDbf.setNumberOfCh((byte)i);
            fieldDbf.setIdentificator((byte)0);
            fieldDbf.setFlagMdx((byte)0);
            fieldDbfs.add(fieldDbf);
        }

        DataDbf dataDbf=new DataDbf(headerDbf,fieldDbfs);
        WriterDbf writerDbf=new WriterDbf(tableName+".dbf");
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
