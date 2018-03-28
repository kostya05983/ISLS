package SQL.Parser;

import GUI.Main;
import SQL.Lib.*;
import javafx.application.Platform;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlerRequest {

    private Main main;

    HandlerRequest(Main main){
        this.main=main;
    }

    protected String[] select(String request){
        return null;
    }

    public void createTable(String request){
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

        request=request.replaceAll("[)]+\\s+[;]",");");

        while(!request.equals(");")) {
            size=0;
            fieldsNames.add( request.substring(0,request.indexOf(" ")));
            request=request.substring(request.indexOf(" ")).trim();

            type=request.substring(0,request.indexOf(")")+2);
            request=request.substring(request.indexOf(")")+2);
            if(type.contains(",") &&type.indexOf(",")!=type.length()-1){
                String type_F = type.substring(type.indexOf("("),type.indexOf(","));
                Platform.runLater(() ->
                        main.outText(type_F));
                size+=Short.parseShort(type.substring(type.indexOf("(")+1,type.indexOf(",")));
                size++;
                size+=Short.parseShort(type.substring(type.indexOf(",")+1,type.indexOf(")")));
                type=type.substring(0,type.indexOf("("));
            }else {
                size += Short.parseShort(type.substring(type.indexOf("(") + 1, type.indexOf(")")));
                type = type.substring(0, type.indexOf("("));
            }

            sizes.add(size);
            if(type.toLowerCase().equals("character"))
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

        for (Byte size1 : sizes) sum += size1;



        HeaderDbf headerDbf=new HeaderDbf();

        headerDbf.setSignature((byte)4);
        headerDbf.setData();
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
        writerDbf.write(dataDbf);
        writerDbf.close();

        Platform.runLater(() -> {
            main.clearTable();
            main.setAllColumns(dataDbf.getAllColumns());
            main.outText("Успешно");
        });

    }

    protected void createIndex(String request){//работа с байтами

    }

    protected void insertInto(String request){//можно сделать примитив

    }

    protected void update(String request){//Можно сделать примитив

    }

    protected void delete(String request){//можнос делать примитив
        String table_name=request.substring(request.indexOf(" FROM ")+5,request.indexOf("\n")).trim();
        ArrayList<String> conditions = new ArrayList<>(getConditions(request));//Все условия, которые после WHERE
        DataDbf dataDBF=new DataDbf();
        ReaderDbf reader=new ReaderDbf(table_name + ".dbf");
        dataDBF=reader.read();
        reader.close();
        Column[] columns=dataDBF.getAllColumns();
        for (String s:conditions
                ) {
            deleteRecords(s,dataDBF);
        }
        dataDBF.setAllColumns(columns);
        setDBF(dataDBF,table_name);
    }

    protected void alterTable(String request){
        int type = -50;
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
        DataDbf dataDbf = readerDbf.read();
        readerDbf.close();
        //Изменение заголовка
        //Настройка даты
        dataDbf.headerDbf.setData();
        //-------------------------------------------------------------------------------------------------------
        switch (type)
        {
            case 1://ADD работает
            {
                //Добавление с помощью setAllColumns
                boolean check = false;

                //System.out.println("ADD");
                Platform.runLater(() ->
                        main.outText("ADD"));
                for(int i = 0; i < dataDbf.fieldsDbf.size(); i++) {
                    String namef = new String(dataDbf.fieldsDbf.get(i).getNameFiled());
                    namef = namef.trim();
                    if (Name_Column.compareTo(namef) == 0) {
                        check = true;
                    }
                }
                if(!check) {
                    Column[] columns = dataDbf.getAllColumns();
                    int length = columns.length;
                    Column[] newcolumns = new Column[length + 1];
                    for (int i = 0; i < length; i++)
                        newcolumns[i] = columns[i];
                    String[] data = new String[dataDbf.recordsDbf.size()];
                    Column column = new Column(Type_Column, Name_Column, data, size_data);
                    newcolumns[length] = column;
                    dataDbf.setAllColumns(newcolumns);
                }
                else
                    Platform.runLater(() ->
                            main.error("Поле с таким именем уже\nсуществует"));
                break;
            }
            case 2://DROP работает
            {
                int check = 0;
                for(int i = 0; i<dataDbf.fieldsDbf.size(); i++) {
                    String namef = new String(dataDbf.fieldsDbf.get(i).getNameFiled());
                    namef = namef.trim();
                    if (Name_Column.compareTo(namef) == 0) {
                        check = 1;
                        Platform.runLater(() ->
                                main.outText("FIND"));
                        //System.out.println("FIND");
                        dataDbf.fieldsDbf.remove(i);
                        dataDbf.recordsDbf.remove(i);
                        break;
                    }
                    else
                        check = 2;
                }
                if(check == 2)
                    Platform.runLater(() ->
                            main.error("Не найдена колонка"));
                break;
            }
            case 3://MODIFY работает до размера в 10
            {
                //System.out.println("MODIFY");
                Platform.runLater(() ->
                        main.outText("MODIFY"));
                int check = 0;
                for(int i = 0; i<dataDbf.fieldsDbf.size(); i++)
                {
                    String namef = new String(dataDbf.fieldsDbf.get(i).getNameFiled());
                    namef = namef.trim();
                    if (Name_Column.compareTo(namef) == 0) {
                        check = 1;
                        //System.out.println("FIND");
                        Platform.runLater(() ->
                                main.outText("FIND"));
                        dataDbf.fieldsDbf.get(i).setTypeField(Type_Column.code);
                        dataDbf.fieldsDbf.get(i).setSizeField((byte)size_data);
                        break;
                    }
                    else
                        check = 2;
                }
                if(check == 2)
                    Platform.runLater(() ->
                            main.error("Не найдена колонка"));
                break;
            }
        }

        WriterDbf writerDbf = new WriterDbf(Name_Table + ".dbf");
        writerDbf.write(dataDbf);
        writerDbf.close();

        Platform.runLater(() -> {
            main.clearTable();
            main.setAllColumns(dataDbf.getAllColumns());
            main.outText("Успешно");
        });
    }

    protected void truncate(String request){
        String table_name;
        table_name=request.substring(request.indexOf(" TABLE ")+6,request.indexOf(";")).trim();
        DataDbf dataDBF=new DataDbf();
        ReaderDbf reader=new ReaderDbf(table_name + ".dbf");
        dataDBF=reader.read();
        reader.close();
        for (RecordDbf rd:dataDBF.recordsDbf
                ) {
            dataDBF.recordsDbf.remove(rd);
        }
        dataDBF.headerDbf.setNumberOfRecords(0);
        setDBF(dataDBF,table_name);

    }

    protected void dropTable(String request){
        request=request.trim();
        request=request.substring(request.indexOf(" ")).trim();
        request=request.substring(request.indexOf(" ")).trim();

        WriterDbf writerDbf=new WriterDbf();
        writerDbf.deleteFile(request);

        Platform.runLater(() -> {
            main.clearTable();
            main.outText("Успешно");
        });
    }

    protected void dropIndex(String request){

    }


    private static boolean checkChar(String condition,String cellData,String operator){
        boolean b=false;
        switch (operator) {
            case "=":
                b = condition.substring(condition.indexOf("'") + 1, condition.lastIndexOf("'")).equals(cellData);
                break;
            case "<>":
                b = !(condition.substring(condition.indexOf("'") + 1, condition.lastIndexOf("'")).equals(cellData));
                break;
            case "<":
                b = condition.substring(condition.indexOf("'") + 1, condition.lastIndexOf("'")).compareTo(cellData) > 0;
                break;
            case ">":
                b = condition.substring(condition.indexOf("'") + 1, condition.lastIndexOf("'")).compareTo(cellData) < 0;
                break;
            case "<=":
                b = condition.substring(condition.indexOf("'") + 1, condition.lastIndexOf("'")).compareTo(cellData) >= 0;
                break;
            case ">=":
                b = condition.substring(condition.indexOf("'") + 1, condition.lastIndexOf("'")).compareTo(cellData) <= 0;
                break;
            case " BETWEEN ":
                String val1 = condition.substring(condition.indexOf(" BETWEEN ") + 9, condition.indexOf(" AND ")).trim();
                String val2 = condition.substring(condition.indexOf(" AND ") + 5).trim();
                b = (cellData.compareTo(val1) >= 0 && cellData.compareTo(val2) <= 0) || (cellData.compareTo(val1) <= 0 && cellData.compareTo(val2) >= 0);
                break;
            case " LIKE ":
                String strReg = "^" + condition.substring(condition.indexOf("'") + 1,
                        condition.lastIndexOf("'")).replace("%", "*").replace("_", ".") + "$";
                b = regLike(strReg, cellData);
                break;
            case " IN ":
                ArrayList<String> val = new ArrayList<>();
                String v = condition.substring(condition.indexOf("(") + 1, condition.indexOf(")")).trim();
                while (val.contains(",")) {
                    val.add(v.substring(0, v.indexOf(",")));
                    v = v.substring(v.indexOf(",") + 1).trim();
                }
                val.add(v);
                for (String vv : val
                        ) {
                    if (vv.substring(vv.indexOf("'") + 1, vv.lastIndexOf("'")).equals(cellData)) {
                        b = true;
                    }
                }
                break;
        }
        return b;
    }

    private static boolean checkInt(String condition,String cellData,String operator){
        boolean b=false;
        BigInteger cond=new BigInteger(condition.substring(condition.indexOf(operator) + operator.length()).trim());
        BigInteger cell = new BigInteger(cellData);
        switch (operator) {
            case "=":
                b = cond.compareTo(cell)==0;
                break;
            case "<>":
                b = cond.compareTo(cell)!=0;
                break;
            case "<":
                b = cond.compareTo(cell) ==1;
                break;
            case ">":
                b = cond.compareTo(cell) ==-1;
                break;
            case "<=":
                b = cond.compareTo(cell) != -1;
                break;
            case ">=":
                b = cond.compareTo(cell) != 1;
                break;
            case " BETWEEN ":
                BigInteger val1 = new BigInteger(condition.substring(condition.indexOf(" BETWEEN ") + 9, condition.indexOf(" AND ")).trim());
                BigInteger val2 = new BigInteger(condition.substring(condition.indexOf(" AND ") + 5).trim());
                b = (cell.compareTo(val1) >= 0 && cell.compareTo(val2) <= 0) || (cell.compareTo(val1) <= 0 && cell.compareTo(val2) >= 0);
                break;
            case " LIKE ":
                String strReg = "^" + condition.substring(condition.indexOf("'") + 1,
                        condition.lastIndexOf("'")).replace("%", "*").replace("_", ".") + "$";
                b = regLike(strReg, cellData);
                break;
            case " IN ":
                ArrayList<BigInteger> val = new ArrayList<>();
                String v = condition.substring(condition.indexOf("(") + 1, condition.indexOf(")")).trim();
                while (v.contains(",")) {
                    val.add(new BigInteger(v.substring(0, v.indexOf(","))));
                    v = v.substring(v.indexOf(",") + 1).trim();
                }
                val.add(new BigInteger(v));
                for (BigInteger vv : val
                        ) {
                    if (vv.compareTo(cell)==0) {
                        b = true;
                    }
                }
                break;
        }
        return b;
    }

    private static boolean checkFloat(String condition,String cellData,String operator){
        boolean b=false;
        BigDecimal cond=new BigDecimal(condition.substring(condition.indexOf(operator) + operator.length()).trim().replace(",","."));
        BigDecimal cell = new BigDecimal(cellData);
        switch (operator) {
            case "=":
                b = cond.compareTo(cell)==0;
                break;
            case "<>":
                b = cond.compareTo(cell)!=0;
                break;
            case "<":
                b = cond.compareTo(cell) ==1;
                break;
            case ">":
                b = cond.compareTo(cell) ==-1;
                break;
            case "<=":
                b = cond.compareTo(cell) != -1;
                break;
            case ">=":
                b = cond.compareTo(cell) != 1;
                break;
            case " BETWEEN ":
                BigDecimal val1 = new BigDecimal(condition.substring(condition.indexOf(" BETWEEN ") + 9, condition.indexOf(" AND ")).trim());
                BigDecimal val2 = new BigDecimal(condition.substring(condition.indexOf(" AND ") + 5).trim());
                b = (cell.compareTo(val1) >= 0 && cell.compareTo(val2) <= 0) || (cell.compareTo(val1) <= 0 && cell.compareTo(val2) >= 0);
                break;
            case " LIKE ":
                String strReg = "^" + condition.substring(condition.indexOf("'") + 1,
                        condition.lastIndexOf("'")).replace("%", "*").replace("_", ".") + "$";
                b = regLike(strReg, cellData);
                break;
            case " IN ":
                ArrayList<BigDecimal> val = new ArrayList<>();
                String v = condition.substring(condition.indexOf("(") + 1, condition.indexOf(")")).trim();
                while (v.contains(",")) {
                    val.add(new BigDecimal(v.substring(0, v.indexOf(","))));
                    v = v.substring(v.indexOf(",") + 1).trim();
                }
                val.add(new BigDecimal(v));
                for (BigDecimal vv : val
                        ) {
                    if (vv.compareTo(cell)==0) {
                        b = true;
                    }
                }
                break;
        }
        return b;
    }

    private static ArrayList<String> getConditions(String str){
        ArrayList<String> conditions = new ArrayList<>();
        String condition;
        String tmp;
        condition=str.substring(str.indexOf(" WHERE ")+6,str.indexOf(";")).trim();
        while (condition.contains(" AND ")) {
            if(condition.substring(condition.indexOf(" ")+1,condition.indexOf(" ")+8).equals("BETWEEN")){
                tmp=condition.substring(0,condition.indexOf(" AND ")+5);
                condition=condition.substring(condition.indexOf(" AND ")+5).trim();
                tmp+=condition.substring(0,condition.indexOf(" "));
                conditions.add(tmp);
                condition=condition.substring(condition.indexOf(" ")).trim();
                if(condition.substring(0,4).equals("AND ")){
                    condition=condition.substring(4).trim();
                }
            }
            else {
                conditions.add(condition.substring(0, condition.indexOf(" AND ")).trim());
                condition = condition.substring(condition.indexOf(" AND ") + 4).trim();
            }
        }
        conditions.add(condition.trim());
        return conditions;
    }

    private static void remove(DataDbf dataDbf,Column c,String sss){
        dataDbf.recordsDbf.remove(Arrays.asList(c.data).indexOf(sss));
        dataDbf.headerDbf.setNumberOfRecords(dataDbf.headerDbf.getLengthOfRecord()-1);
    }

    private static boolean reg(String s){
        Pattern p=Pattern.compile("^'*'$");
        Matcher m=p.matcher(s);
        return m.matches();
    }

    private static boolean regArr(String condition ){
        boolean k=true;
        ArrayList<String> val = new ArrayList<>();
        String v = condition.substring(condition.indexOf("(") + 1, condition.indexOf(")")).trim();
        while (val.contains(",")) {
            val.add(v.substring(0, v.indexOf(",")));
            v = v.substring(v.indexOf(",") + 1).trim();
        }
        val.add(v);
        for (String ss:val
                ) {
            if(!reg(ss)) {
                k=false;
            }
        }
        return k;
    }

    private static boolean regLike(String s,String sArr){
        Pattern p=Pattern.compile(s);
        Matcher m=p.matcher(sArr);
        return  m.matches();
    }

    private void deleteRecords(String str,DataDbf dataDbf){
        if(str.contains("=")){
            deletE(str,"=",dataDbf);
        }
        else if(str.contains("<>")){
            deletE(str,"<>",dataDbf);
        }
        else if(str.contains("<")){
            deletE(str,"<",dataDbf);
        }
        else if(str.contains("<=")){
            deletE(str,"<=",dataDbf);
        }
        else if(str.contains(">")){
            deletE(str,">",dataDbf);
        }
        else if(str.contains(">=")){
            deletE(str,">=",dataDbf);
        }
        else if(str.contains(" BETWEEN ")){
            deletE(str," BETWEEN ",dataDbf);
        }
        else if(str.contains(" LIKE ")){
            deletE(str," LIKE ",dataDbf);
        }
        else if(str.contains(" IN ")){
            if(regArr(str)) {
                deletE(str, " IN ", dataDbf);
            }
            else{
                Platform.runLater(() ->
                        main.error("Ошибка в deleteRecords"));
            }
        }
    }

    private void deletE(String condition,String operator,DataDbf dataDbf){
        String columnName=condition.substring(0,condition.indexOf(operator)).trim();
        for (Column c:dataDbf.getAllColumns()
                ) {
            if(c.title.equals(columnName)) {
                switch (c.type){
                    case Character:
                        if(reg(condition.substring(condition.indexOf(operator)+operator.length()).trim())) {
                            for (String sss : c.data
                                    ) {
                                if (checkChar(condition,sss,operator)) {
                                    remove(dataDbf,c,sss);
                                }
                            }
                        }
                        else{
                            Platform.runLater(() ->
                                    main.error("Ошибка в deletE"));
                        }
                        break;
                    case Integer:
                        for (String sss : c.data
                                ) {
                            if (checkInt(condition,sss,operator)) {
                                remove(dataDbf,c,sss);
                            }
                        }
                        break;
                    case Float:
                        for (String sss : c.data
                                ) {
                            if (checkFloat(condition,sss,operator)) {
                                remove(dataDbf,c,sss);
                            }
                        }
                        break;
                }
            }
        }
    }

    private static void setDBF(DataDbf dataDBF,String table_name){
        Date date=new Date();
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        dataDBF.headerDbf.setDay((byte)c.get(Calendar.DAY_OF_MONTH));
        dataDBF.headerDbf.setMonth((byte)c.get(Calendar.MONTH));
        dataDBF.headerDbf.setYear((byte)(c.get(Calendar.YEAR)%100));
        WriterDbf writerDbf=new WriterDbf(table_name+".dbf");
        writerDbf.write(dataDBF);
        writerDbf.close();
    }
}
