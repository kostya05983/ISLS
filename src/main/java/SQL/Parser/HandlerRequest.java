package SQL.Parser;

import GUI.Main;
import SQL.Lib.AdditionalInstruments.Column;
import SQL.Lib.DataHandler.ReaderDbf;
import SQL.Lib.DataHandler.WriterDbf;
import SQL.Lib.Dbf.*;
import SQL.Lib.Indexes.DataIdx;
import javafx.application.Platform;


import javax.swing.text.html.parser.Parser;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class HandlerRequest {

    private Main main;

    HandlerRequest(Main main) {
        this.main = main;
    }

    void select(String request) throws IOException {
        request=request.substring(request.toUpperCase().indexOf("SELECT")+7);
        DataDbf dataDbf;
        ArrayList<RecordDbf> resultRecords;

        if(request.contains("*")){
            request=request.substring(request.toUpperCase().indexOf("FROM")+5);
            String tableName=request.substring(0,request.indexOf(" ")).trim();
            ReaderDbf readerDbf=new ReaderDbf(tableName+".dbf");
            dataDbf=readerDbf.read();
            if(request.toUpperCase().contains("WHERE")) {
                request = request.substring(request.toUpperCase().indexOf("WHERE") + 6).replaceAll("[;]", "");
                Where where = new Where();
                ArrayList<Integer> indexes = where.getRecs(request, dataDbf);
                resultRecords = new ArrayList<>();

                for (Integer index : indexes) {
                    resultRecords.add(dataDbf.recordsDbf.get(index));
                }
                dataDbf = new DataDbf(dataDbf.headerDbf, dataDbf.fieldsDbf, resultRecords);
            }

        }else{
            request=request.replaceAll("[,]","");
            String[] namesColumns=request.substring(0,request.toUpperCase().indexOf("FROM")).trim().split("[ ]");
            request=request.substring(request.indexOf("FROM")+5).trim();
            String tableName=request.substring(0,request.indexOf("WHERE")).trim();


            ReaderDbf readerDbf=new ReaderDbf(tableName+".dbf");
            dataDbf=readerDbf.read();
            dataDbf=dataDbf.selectColumns(namesColumns);
            if(request.toUpperCase().contains("WHERE")) {
                request = request.substring(request.indexOf("WHERE") + 6).replaceAll("[ ;]", "");
                Where where = new Where();
                ArrayList<Integer> indexes = where.getRecs(request, dataDbf);
                resultRecords = new ArrayList<>();

                for (Integer index : indexes) {
                    resultRecords.add(dataDbf.recordsDbf.get(index));
                }
                dataDbf = new DataDbf(dataDbf.headerDbf, dataDbf.fieldsDbf, resultRecords);
            }
        }

        DataDbf finalDataDbf = dataDbf;
        Platform.runLater(() -> {
            main.clearTable();
            main.setAllColumns(finalDataDbf.getColumnsforShow());
            main.outText("Успешно");
        });
    }

    void createTable(String request) throws IOException {
        request = request.substring(request.indexOf(" ") + 1);
        request = request.substring(request.indexOf(" ") + 1);
        String tableName = request.substring(0, request.indexOf("(")).trim();
        request = request.substring(request.indexOf("(") + 1);

        ArrayList<String> fieldsNames = new ArrayList<>();
        ArrayList<TypesOfFields> types = new ArrayList<>();
        ArrayList<Byte> sizes = new ArrayList<>();

        String type;
        request = request.trim();
        byte size;
        String end="\\s*\\)\\s*;\\s*";

        while (!request.substring(request.indexOf(")")).matches(end)) {
            size = 0;
            fieldsNames.add(request.substring(0, request.indexOf(" ")));
            request = request.substring(request.indexOf(" ")).trim();

            type = request.substring(0, request.indexOf(")")+1).trim();
            request = request.substring(request.indexOf(")")+1).trim();
            if (type.contains(",")) {
                String type_F = type.substring(type.indexOf("("), type.indexOf(","));
                Platform.runLater(() ->
                        main.outText(type_F));
                size += Short.parseShort(type.substring(type.indexOf("(") + 1, type.indexOf(",")).trim());
                size++;
                size += Short.parseShort(type.substring(type.indexOf(",") + 1, type.indexOf(")")).trim());
                type = type.substring(0, type.indexOf("(")).trim();
            } else {
                size += Short.parseShort(type.substring(type.indexOf("(") + 1, type.indexOf(")")).trim());
                type = type.substring(0, type.indexOf("(")).trim();
            }

            sizes.add(size);
            if (type.toLowerCase().equals("character"))
                types.add(TypesOfFields.Character);
            if (type.toLowerCase().equals("integer")) {
                types.add(TypesOfFields.Integer);
            }
            if (type.toLowerCase().equals("float")) {
                types.add(TypesOfFields.Float);
            }

            request=request.substring(request.indexOf(",")+1);
            request = request.trim();
        }

        short sum = 0;

        for (Byte size1 : sizes) sum += size1;

        HeaderDbf headerDbf = new HeaderDbf();

        headerDbf.setSignature((byte) 4);
        headerDbf.setData();
        headerDbf.setNumberOfRecords(0);
        headerDbf.setLengthOfTitle((short) (32 * fieldsNames.size()));
        headerDbf.setLengthOfRecord(sum);
        headerDbf.setFlagTransaction((byte) 0);
        headerDbf.setFlagEncryption((byte) 0);

        FieldDbf fieldDbf;
        ArrayList<FieldDbf> fieldDbfs = new ArrayList<>();
        for (int i = 0; i < fieldsNames.size(); i++) {
            fieldDbf = new FieldDbf();
            fieldDbf.setNameField(fieldsNames.get(i));
            fieldDbf.setTypeField(types.get(i).code);
            fieldDbf.setSizeField(sizes.get(i));
            fieldDbf.setNumberOfCh((byte) i);
            fieldDbf.setIdentificator((byte) 0);
            fieldDbf.setFlagMdx((byte) 0);
            fieldDbfs.add(fieldDbf);
        }

        DataDbf dataDbf = new DataDbf(headerDbf, fieldDbfs);
        WriterDbf writerDbf = new WriterDbf(tableName + ".dbf");
        writerDbf.write(dataDbf);
        writerDbf.close();

        Platform.runLater(() -> {
            main.clearTable();
            main.setAllColumns(dataDbf.getColumnsforShow());
            main.outText("Успешно");
        });

    }

    void createIndex(String request) throws ParserException, IOException{
        request = request.substring(request.toLowerCase().indexOf("index") + 6);
        String indexName = request.substring(0, request.indexOf(" "));
        request = request.substring(request.toLowerCase().indexOf("on") + 3);
        String tableName = request.substring(0, request.indexOf(" "));
        request = request.substring(request.indexOf("(") + 1);
        request = request.replaceAll(" ", "");
        String nameField = request.substring(0, request.indexOf(")"));

        DataDbf dataDbf;
        ReaderDbf readerDbf = new ReaderDbf(tableName + ".dbf");
        dataDbf = readerDbf.read();
        readerDbf.close();

        boolean flag = false;
        dataDbf.headerDbf.setFlagMDX((byte) 1);
        for (int i = 0; i < dataDbf.fieldsDbf.size(); i++) {
            if (dataDbf.getPartOfRecord(dataDbf.fieldsDbf.get(i).getNameField()).equals(nameField)) {
                flag = true;
                dataDbf.fieldsDbf.get(i).setFlagMdx((byte) 1);
                break;
            }
        }
        if (!flag) throw new ParserException("Не совпало имя createIndex");

        WriterDbf writerDbf = new WriterDbf(tableName + ".dbf");
        writerDbf.write(dataDbf);
        writerDbf.close();

        DataIdx dataIdx = new DataIdx(nameField, dataDbf);
        writerDbf = new WriterDbf(indexName + ".idx");
        writerDbf.write(dataIdx);
        writerDbf.close();
    }

    void insertInto(String request) throws IOException {

        //берём данные команлды
        request=request.substring(request.toUpperCase().indexOf("INSERT INTO")+11);

        //берём имя таблицы
        String table_name = request.substring(0,request.toUpperCase().indexOf("(")).trim();

        //названия полей
        String[] name_pole = request.substring(request.toUpperCase().indexOf("(")+1,request.toUpperCase().indexOf(")")).trim().split("[,]");

        request=request.substring(request.toUpperCase().indexOf("VALUE")+5).trim();

        //значения после value
        String[] value = request.substring(request.toUpperCase().indexOf("(")+1,request.toUpperCase().indexOf(")")).trim().split("[,]");

        DataDbf dataDbf;

        try {
            //открываем выбранную таблицу
            ReaderDbf readerDbf=new ReaderDbf(table_name+".dbf");
            dataDbf=readerDbf.read();
            //dataDbf=dataDbf.selectColumns(name_pole);
            readerDbf.close();
            //Настройка даты
            dataDbf.headerDbf.setData();

            //Взятие всех колонок
            Column[] columns = dataDbf.getAllColumns();

            //переменная ошибки
            boolean check_error = false;

            //в выбранной таблице определяем тип поля и преобразуем в него нужный value,
            //а потом в строку для хранения
            for (Column column : columns)
            {
                boolean existence = false;
                int k;

                //ищем совпадающее поле по имени
                for (k = 0; k < name_pole.length; k++) {
                    if (column.title.trim().equals(name_pole[k].trim())) {
                        existence = true;
                        break;
                    }
                }

                //если Пользователь не ввёл это имя поля и данные для него
                if (!existence) {
                    column.addRecord(" ");
                } else {
                    //преобразуем и сохраняем в найденное поле
                    switch (column.type) {
                        case Integer: {
                            try {
                                String t = String.valueOf(Integer.valueOf(value[k].trim()));
                                //вставляем данные в поле
                                column.addRecord(t);
                            } catch (NumberFormatException e) {
                                check_error = true;
                                Platform.runLater(() ->
                                        main.error("Типы полей должны совпадать\nс типом данных"));
                                main.outText("Не успешно :(\n");
                            }
                            break;
                        }
                        case Float: {
                            try {
                                String t = String.valueOf(Float.valueOf(value[k].trim()));
                                //вставляем данные в поле
                                column.addRecord(t);
                            } catch (NumberFormatException e) {
                                check_error = true;
                                Platform.runLater(() ->
                                        main.error("Типы полей должны совпадать\nс типом данных"));
                                main.outText("Не успешно :(\n");
                            }
                            break;
                        }
                        case Character: {
                            column.addRecord(value[k].trim());
                            break;
                        }
                        default: {
                            check_error = true;
                            Platform.runLater(() ->
                                    main.error());
                            main.outText("Не успешно :(\n");
                        }
                    }
                }
                if (check_error) break;
            }

            if (!check_error) {

                dataDbf.setAllColumns(columns);
                //обновляем колличество записей
                dataDbf.headerDbf.setNumberOfRecords(columns[0].data.length);

                //сохраняем изменения
                WriterDbf writerDbf = new WriterDbf(table_name + ".dbf");
                writerDbf.write(dataDbf);
                writerDbf.close();

                //вывод изменений
                Platform.runLater(() -> {
                    main.clearTable();
                    main.setAllColumns(dataDbf.getColumnsforShow());
                    main.outText("Успешно");
                });
            }
        }
        catch (NumberFormatException e) {
            Platform.runLater(() ->
                    main.error("Не удалось открыть таблицу"));
            main.outText("Не успешно :(\n");
        }

    }

    void update(String request) throws IOException {

        //берём данные команлды
        request=request.substring(request.toUpperCase().indexOf("UPDATE")+6);

        //берём имя таблицы
        String table_name = request.substring(0,request.toUpperCase().indexOf("SET")).trim();

        //пары
        String[] pairs = request.substring(request.toUpperCase().indexOf("SET")+3,request.toUpperCase().indexOf("WHERE")).trim().split("[,]");

        String[] name_pole = new String[pairs.length];
        for (int i = 0; i<pairs.length; i++)
        {
            name_pole[i] = pairs[i].substring(0,request.toUpperCase().indexOf("=")).trim();
            pairs[i] = pairs[i].substring(request.toUpperCase().indexOf("=")+1).trim();
        }

        //значения после where (логическое выражение)
        String logik = request.substring(request.toUpperCase().indexOf("WHERE")+5,request.toUpperCase().indexOf(";")).trim();

        DataDbf dataDbf;

        try {
            //открываем выбранную таблицу
            ReaderDbf readerDbf = new ReaderDbf(table_name + ".dbf");
            dataDbf = readerDbf.read();
            dataDbf = dataDbf.selectColumns(name_pole);
            readerDbf.close();
            //Настройка даты
            dataDbf.headerDbf.setData();

            Column[] columns = dataDbf.getAllColumns();

            Where where = new Where();
            DataDbf toDbf = new DataDbf();

            //проход по списку пар
            for (String pair : pairs) {
                //ищем совпадающее поле по имени в списке колонок
                for (int k = 0; k < pairs.length; k++) {
                    if (columns[k].title.trim().equals(name_pole[k].trim())) {

                        toDbf.setAllColumns(new Column[]{columns[k]});
                        ArrayList<Integer> result = where.getRecs(logik, toDbf);

                        //проход внутри колонки
                        for (Integer f : result) {
                            switch (columns[k].type) {
                                case Integer: {
                                    try {
                                        columns[k].data[f] = String.valueOf(Integer.valueOf(pair.trim()));
                                    } catch (NumberFormatException e) {
                                        main.outText("Не удалось провести одно из изменений\n");
                                    }
                                    break;
                                }
                                case Float: {
                                    try {
                                        columns[k].data[f] = String.valueOf(Float.valueOf(pair.trim()));
                                    } catch (NumberFormatException e) {
                                        main.outText("Не удалось провести одно из изменений\n");
                                    }
                                    break;
                                }
                                case Character: {
                                    columns[k].data[f] = pair;
                                }
                                default: {
                                    main.outText("Не удалось провести одно из изменений\n");
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }

            dataDbf.setAllColumns(columns);

            //сохраняем изменения
            WriterDbf writerDbf = new WriterDbf(table_name + ".dbf");
            writerDbf.write(dataDbf);
            writerDbf.close();

            DataDbf finalDBF = dataDbf;
            //вывод изменений
            Platform.runLater(() -> {
                main.clearTable();
                main.setAllColumns(finalDBF.getColumnsforShow());
                main.outText("Успешно");
            });
        }
        catch (NumberFormatException e)
        {
            Platform.runLater(() ->
                    main.error("Не удалось открыть таблицу"));
            main.outText("Не успешно :(\n");
        }
    }

    void delete(String request) throws IOException {
        String table_name = request.substring(request.indexOf(" FROM ") + 5).trim();
        table_name = table_name.substring(0, table_name.indexOf(" ")).trim();
        Where wh = new Where();
        DataDbf dataDBF;
        ReaderDbf reader = new ReaderDbf(table_name + ".dbf");
        dataDBF = reader.read();
        reader.close();
        Column[] columns = dataDBF.getAllColumns();
        ArrayList<Integer> recs = new ArrayList<>(wh.getRecs(request, dataDBF));
        for (int ind : recs
                ) {
            dataDBF.recordsDbf.remove(ind);
            dataDBF.headerDbf.setNumberOfRecords(dataDBF.headerDbf.getLengthOfRecord() - 1);
        }
        dataDBF.setAllColumns(columns);
        setDBF(dataDBF, table_name);
        WriterDbf writerDbf = new WriterDbf(table_name + ".dbf");
        writerDbf.write(dataDBF);
        writerDbf.close();

        Platform.runLater(() -> {
            main.clearTable();
            main.setAllColumns(dataDBF.getColumnsforShow());
            main.outText("Успешно");
        });
    }

    void alterTable(String request) throws IOException{
        int type = -50;
        int size_data = 0;
        TypesOfFields Type_Column = TypesOfFields.Integer;
        String Type_Action = " ";
        String Name_Table = " ";
        String Name_Column = " ";
        String Type_Data = " ";
        request = request.trim();
        request = request.substring(request.indexOf(" ") + 1).trim();
        request = request.substring(request.indexOf(" ") + 1).trim();
        Name_Table = request.substring(0, request.indexOf(" ")).trim();
        request = request.substring(request.indexOf(" ") + 1).trim();
        Type_Action = request.substring(0, request.indexOf(" ")).trim();
        if (Type_Action.toUpperCase().compareTo("ADD") == 0) {
            type = 1;
            request = request.substring(request.indexOf(" ") + 1).trim();
        }
        if (Type_Action.toUpperCase().compareTo("DROP") == 0) {
            type = 2;
            request = request.substring(request.indexOf(" ") + 1).trim();
            request = request.substring(request.indexOf(" ") + 1).trim();
        }
        if (Type_Action.toUpperCase().compareTo("MODIFY") == 0) {
            type = 3;
            request = request.substring(request.indexOf(" ") + 1).trim();
            request = request.substring(request.indexOf(" ") + 1).trim();
        }
        if (type == 2) {
            Name_Column = request.substring(0, request.indexOf(";")).trim();
        } else {
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

        ReaderDbf readerDbf = new ReaderDbf(Name_Table + ".dbf");
        DataDbf dataDbf = readerDbf.read();
        readerDbf.close();
        //Изменение заголовка
        //Настройка даты
        dataDbf.headerDbf.setData();

        switch (type) {
            case 1://ADD работает
            {
                //Добавление с помощью setAllColumns
                boolean check = false;

                //System.out.println("ADD");
                Platform.runLater(() ->
                        main.outText("ADD"));
                for (int i = 0; i < dataDbf.fieldsDbf.size(); i++) {
                    String namef = new String(dataDbf.fieldsDbf.get(i).getNameField());
                    namef = namef.trim();
                    if (Name_Column.compareTo(namef) == 0) {
                        check = true;
                    }
                }
                if (!check) {
                    Column[] columns = dataDbf.getAllColumns();
                    int length = columns.length;
                    Column[] newcolumns = new Column[length + 1];
                    System.arraycopy(columns, 0, newcolumns, 0, length);
                    String[] data = new String[dataDbf.recordsDbf.size()];
                    Column column = new Column(Type_Column, Name_Column, data, size_data);
                    newcolumns[length] = column;
                    dataDbf.setAllColumns(newcolumns);
                } else
                    Platform.runLater(() ->
                            main.error("Поле с таким именем уже\nсуществует"));
                break;
            }
            case 2://DROP работает
            {
                int check = 0;
                for (int i = 0; i < dataDbf.fieldsDbf.size(); i++) {
                    String namef = new String(dataDbf.fieldsDbf.get(i).getNameField());
                    namef = namef.trim();
                    if (Name_Column.compareTo(namef) == 0) {
                        check = 1;
                        Platform.runLater(() ->
                                main.outText("FIND"));
                        //System.out.println("FIND");
                        dataDbf.fieldsDbf.remove(i);
                        dataDbf.recordsDbf.remove(i);
                        break;
                    } else
                        check = 2;
                }
                if (check == 2)
                    Platform.runLater(() ->
                            main.error("Не найдена колонка"));
                break;
            }
            case 3://MODIFY работает до размера в 10
            {
                Platform.runLater(() ->
                        main.outText("MODIFY"));
                int check = 0;
                for (int i = 0; i < dataDbf.fieldsDbf.size(); i++) {
                    String namef = new String(dataDbf.fieldsDbf.get(i).getNameField());
                    namef = namef.trim();
                    if (Name_Column.compareTo(namef) == 0) {
                        check = 1;
                        //System.out.println("FIND");
                        Platform.runLater(() ->
                                main.outText("FIND"));
                        dataDbf.fieldsDbf.get(i).setTypeField(Type_Column.code);
                        dataDbf.fieldsDbf.get(i).setSizeField((byte) size_data);
                        break;
                    } else
                        check = 2;
                }
                if (check == 2)
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
            main.setAllColumns(dataDbf.getColumnsforShow());
            main.outText("Успешно");
        });
    }

    void truncate(String request) throws IOException{
        String table_name;
        table_name = request.substring(request.indexOf(" TABLE ") + 6, request.indexOf(";")).trim();
        DataDbf dataDBF;
        ReaderDbf reader = new ReaderDbf(table_name + ".dbf");
        dataDBF = reader.read();
        reader.close();

        for(int i=0; i < dataDBF.recordsDbf.size(); i++){
            dataDBF.recordsDbf.remove(i);
        }

        dataDBF.headerDbf.setNumberOfRecords(0);
        setDBF(dataDBF, table_name);

        Platform.runLater(() -> {
            main.clearTable();
            main.setAllColumns(dataDBF.getColumnsforShow());
            main.outText("Успешно");
        });
    }

    void dropTable(String request) {
        request = request.trim();
        request = request.substring(request.indexOf(" ")).trim();
        request = request.substring(request.indexOf(" ")).trim();

        WriterDbf writerDbf = new WriterDbf();
        writerDbf.deleteFile(request);

        Platform.runLater(() -> {
            main.clearTable();
            main.outText("Успешно");
        });
    }

    void dropIndex(String request) throws IOException{
        request = request.substring(request.toLowerCase().indexOf("index") + 6);
        String indexName = request.substring(0, request.indexOf(" "));
        request = request.substring(request.toLowerCase().indexOf("on") + 3).trim();
        String tableName = request.substring(0, request.indexOf(";"));

        ReaderDbf readerDbf = new ReaderDbf(tableName + ".dbf");
        DataDbf dataDbf = readerDbf.read();
        dataDbf.headerDbf.setFlagMDX((byte) 0);
        readerDbf.close();
        WriterDbf writerDbf = new WriterDbf(tableName + ".dbf");
        writerDbf.write(dataDbf);
        writerDbf.close();

        //TODO добавить снятие флага с поля,когда будет готово чтение с ключами
        File file = new File(indexName + ".idx");
        file.delete();
    }

    private static void setDBF(DataDbf dataDBF, String table_name) throws IOException{
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        dataDBF.headerDbf.setDay((byte) c.get(Calendar.DAY_OF_MONTH));
        dataDBF.headerDbf.setMonth((byte) c.get(Calendar.MONTH));
        dataDBF.headerDbf.setYear((byte) (c.get(Calendar.YEAR) % 100));
        WriterDbf writerDbf = new WriterDbf(table_name + ".dbf");
        writerDbf.write(dataDBF);
        writerDbf.close();
    }


}
