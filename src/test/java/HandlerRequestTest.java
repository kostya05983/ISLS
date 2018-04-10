import SQL.Lib.AdditionalInstruments.Column;
import SQL.Lib.DataHandler.ReaderDbf;
import SQL.Lib.DataHandler.WriterDbf;
import SQL.Lib.Dbf.DataDbf;
import SQL.Lib.Dbf.TypesOfFields;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class HandlerRequestTest {

    public static void main(String[] args) {
        /*HandlerRequest handlerRequest=new HandlerRequest();

        handlerRequest.createTable("");*/



        int ch = 1;
        System.out.println("Enter number");

        switch (ch) {
            case 1: {
                alterTable("ALTER TABLE test ADD Name INTEGER(5);");
                break;
            }
            case 2: {
                show();
                break;
            }
            case 3: {
                check();
                break;
            }
        }
    }
    public static void alterTable(String request){
        int index = 0;
        int type = 50;
        int size_data = 0;
        TypesOfFields Type_Column = TypesOfFields.Integer;
        String Type_Action = " ";
        String Name_Table = " ";
        String Name_Column = " ";
        String Type_Data = " ";
        request = request.trim();
        request = request.substring(request.indexOf(" ")+1).trim();
        request = request.substring(request.indexOf(" ")+1).trim();
        Name_Table = request.substring(0,request.indexOf(" ")).trim();
        request = request.substring(request.indexOf(" ")+1).trim();
        Type_Action = request.substring(0,request.indexOf(" ")).trim();
        if(Type_Action.toUpperCase().compareTo("ADD") == 0)
        {
            type = 1;
            request = request.substring(request.indexOf(" ")+1).trim();
        }
        if(Type_Action.toUpperCase().compareTo("DROP") == 0)
        {
            type = 2;
            request = request.substring(request.indexOf(" ")+1).trim();
            request = request.substring(request.indexOf(" ")+1).trim();
        }
        if(Type_Action.toUpperCase().compareTo("MODIFY") == 0)
        {
            type = 3;
            request = request.substring(request.indexOf(" ")+1).trim();
            request = request.substring(request.indexOf(" ")+1).trim();
        }
        if(type == 2)
        {
            Name_Column = request.substring(0,request.indexOf(";")).trim();
        }
        else {
            Name_Column = request.substring(0, request.indexOf(" ")).trim();
            request = request.substring(request.indexOf(" ") + 1).trim();
            Type_Data = request.substring(0, request.indexOf("(")).trim();
            size_data = Integer.parseInt(request.substring(request.indexOf("(") + 1, request.indexOf(")")).trim());
            if (Type_Data.toUpperCase().compareTo("CHARACTER") == 0)
                Type_Column = TypesOfFields.Character;
            if (Type_Data.toUpperCase().compareTo("FLOAT") == 0)
                Type_Column = TypesOfFields.Float;
            if (Type_Data.toUpperCase().compareTo("INTEGER") == 0)
                Type_Column = TypesOfFields.Integer;
        }
        //System.out.println(Name_Table);
        //System.out.println(Name_Column);
        //System.out.println(Type_Data);
        //System.out.println(size_data);
        //-------------------------------------------------------------------------------------------------------


        ReaderDbf readerDbf = new ReaderDbf(Name_Table+".dbf");
        DataDbf dataDbf = new DataDbf();
        dataDbf = readerDbf.read();
        readerDbf.close();
        //Изменение заголовка
        //Настройка даты
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(System.currentTimeMillis());
        dataDbf.headerDbf.setYear((byte)(gregorianCalendar.get(Calendar.YEAR)%1000));
        dataDbf.headerDbf.setMonth((byte)gregorianCalendar.get(Calendar.MONTH));
        dataDbf.headerDbf.setDay((byte)gregorianCalendar.get(Calendar.DAY_OF_MONTH));

        //-------------------------------------------------------------------------------------------------------
        switch (type)
        {
            case 1://ADD
            {
                //Добавление с помощью setAllColumns
                System.out.println("ADD");
                Column[] columns = dataDbf.getAllColumns();
                int length = columns.length;
                Column[] newcolumns = new Column[length+1];
                for (int i = 0;i < length; i++)
                    newcolumns[i] = columns[i];
                String[] data = new String[dataDbf.recordsDbf.size()];//????????????????????????
                Column column = new Column(Type_Column,Name_Column,data,size_data);
                newcolumns[length] = column;
                dataDbf.setAllColumns(newcolumns);

                /*еще один вариант добавления
                FieldDbf fielddbf = new FieldDbf();
                fielddbf.setNameField(Name_Table);
                fielddbf.setTypeField(Type_Column.code);
                fielddbf.setSizeField((byte)size_data);
                fielddbf.setNumberOfCh((byte)(dataDbf.fieldsDbf.size()-1));
                fielddbf.setIdentificator((byte) 0);
                fielddbf.setFlagMdx((byte) 0);
                dataDbf.fieldsDbf.add(fielddbf);
                byte[] data = new byte[dataDbf.recordsDbf.get(0).getData().length];
                RecordDbf recordDbf=new RecordDbf();
                recordDbf.setHeaderByte((byte)'*');
                recordDbf.setData(data);
                dataDbf.recordsDbf.add(recordDbf);
                */
                break;
            }
            case 2://DROP работает
            {
                int check = 0;
                for(int i = 0; i<dataDbf.fieldsDbf.size(); i++) {
                    //int i = 0;
                    String namef = new String(dataDbf.fieldsDbf.get(i).getNameField());
                    namef = namef.trim();
                    //System.out.println(namef);
                    if (Name_Column.compareTo(namef) == 0) {
                        check = 1;
                        System.out.println("FIND");
                        dataDbf.fieldsDbf.remove(i);
                        dataDbf.recordsDbf.remove(i);
                        break;
                    }
                    else
                        check = 2;
                }
                if(check == 2)
                    System.out.println("error");
                break;
            }
            case 3://MODIFY работает до размера в 10
            {
                System.out.println("MODIFY");
                int check = 0;
                for(int i = 0; i<dataDbf.fieldsDbf.size(); i++)
                {
                    String namef = new String(dataDbf.fieldsDbf.get(i).getNameField());
                    namef = namef.trim();
                    if (Name_Column.compareTo(namef) == 0) {
                        check = 1;
                        System.out.println("FIND");
                        dataDbf.fieldsDbf.get(i).setTypeField(Type_Column.code);
                        dataDbf.fieldsDbf.get(i).setSizeField((byte)size_data);
                        break;
                    }
                    else
                        check = 2;
                }
                if(check == 2)
                    System.out.println("error");
                break;
            }
        }

        WriterDbf writerDbf = new WriterDbf(Name_Table + ".dbf");
        writerDbf.write(dataDbf);
        writerDbf.close();

    }
    public static void show(){
        ReaderDbf readerDbf = new ReaderDbf("test.dbf");
        DataDbf dataDbf = new DataDbf();
        dataDbf = readerDbf.read();
        for(int i = 0; i<dataDbf.fieldsDbf.size(); i++) {
            String namef = new String(dataDbf.fieldsDbf.get(i).getNameField());
            System.out.println("Field:" + namef);
            String namer = new String(dataDbf.recordsDbf.get(i).getByteCode());
            System.out.println("Record:" + namer);
        }

    }
    public static void check()
    {
        ReaderDbf readerDbf = new ReaderDbf("test.dbf");
        DataDbf dataDbf = new DataDbf();
        dataDbf = readerDbf.read();
        byte ch = dataDbf.fieldsDbf.get(0).getTypeField();
        byte si = dataDbf.fieldsDbf.get(0).getSizeField();
        System.out.println("Byte:" + ch + "size" + si);
    }
}