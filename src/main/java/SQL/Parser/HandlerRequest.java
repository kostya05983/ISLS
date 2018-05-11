package SQL.Parser;

import GUI.Main;
import SQL.Lib.AdditionalInstruments.Column;
import SQL.Lib.DataHandler.ReaderDbf;
import SQL.Lib.DataHandler.WriterDbf;
import SQL.Lib.Dbf.*;
import SQL.Lib.Indexes.DataIdx;
import javafx.application.Platform;


import java.io.File;
import java.io.IOException;
import java.util.*;

class HandlerRequest {

    private Main main;

    HandlerRequest(Main main) {
        this.main = main;
    }

    void select(String request) throws IOException, ParserException {
        request = request.substring(request.toUpperCase().indexOf("SELECT") + 7);
        DataDbf dataDbf;
        ArrayList<RecordDbf> resultRecords;

        if (request.contains("*")) {
            request = request.substring(request.toUpperCase().indexOf("FROM") + 5);
            String tableName;
            if (request.contains(" "))
                tableName = request.substring(0, request.indexOf(" ")).trim();
            else
                tableName = request.substring(0, request.indexOf(";")).trim();
            var readerDbf = new ReaderDbf(tableName + ".dbf");
            dataDbf = readerDbf.read();
            if (request.toUpperCase().contains("WHERE")) {
                request = request.substring(request.toUpperCase().indexOf("WHERE") + 6).replaceAll("[;]", "");
                var where = new Where();
                ArrayList<Integer> indexes = where.getRecs(request, dataDbf);
                resultRecords = new ArrayList<>();

                for (Integer index : indexes) {
                    resultRecords.add(dataDbf.recordsDbf.get(index));
                }
                dataDbf = new DataDbf(dataDbf.headerDbf, dataDbf.fieldsDbf, resultRecords);
            }

        } else {
            String[] namesColumns = request.substring(0, request.toUpperCase().indexOf("FROM")).trim().split("[,]");
            request = request.substring(request.indexOf("FROM") + 5).trim();
            String tableName;
            if (request.contains("WHERE"))
                tableName = request.substring(0, request.indexOf("WHERE")).trim();
            else
                tableName = request.substring(0, request.indexOf(";")).trim();

            var readerDbf = new ReaderDbf(tableName + ".dbf");
            dataDbf = readerDbf.read();
            dataDbf = dataDbf.selectColumns(namesColumns);
            if (request.toUpperCase().contains("WHERE")) {
                request = request.substring(request.indexOf("WHERE") + 6).replaceAll("[ ;]", "");
                var where = new Where();
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
            main.outText("SELECT выполнилось успешно");
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
        String end = "\\s*\\)\\s*;\\s*";

        while (!request.substring(request.indexOf(")")).matches(end)) {
            size = 0;
            fieldsNames.add(request.substring(0, request.indexOf(" ")));
            request = request.substring(request.indexOf(" ")).trim();

            type = request.substring(0, request.indexOf(")") + 1).trim();
            request = request.substring(request.indexOf(")") + 1).trim();
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

            request = request.substring(request.indexOf(",") + 1);
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
            fieldDbf.setIdentification((byte) 0);
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
            main.outText("CREATE TABLE выполнилось успешно");
        });

    }

    void createIndex(String request) throws ParserException, IOException {
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
        request = request.substring(request.toUpperCase().indexOf("INSERT INTO") + 11);

        //берём имя таблицы
        String table_name = request.substring(0, request.toUpperCase().indexOf("(")).trim();

        //названия полей
        String[] name_pole = request.substring(request.toUpperCase().indexOf("(") + 1, request.toUpperCase().indexOf(")")).trim().split("[,]");

        request = request.substring(request.toUpperCase().indexOf("VALUE") + 5).trim();

        //значения после value
        String[] value = request.substring(request.toUpperCase().indexOf("(") + 1, request.toUpperCase().indexOf(")")).trim().split("[,]");

        DataDbf dataDbf;

        try {
            //открываем выбранную таблицу
            var readerDbf = new ReaderDbf(table_name + ".dbf");
            dataDbf = readerDbf.read();
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
            for (Column column : columns) {
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
                                var t = String.valueOf(Integer.valueOf(value[k].trim()));
                                //вставляем данные в поле
                                column.addRecord(t);
                            } catch (NumberFormatException e) {
                                check_error = true;
                                Platform.runLater(() ->
                                        main.error("Типы полей должны совпадать\nс типом данных"));
                                main.outText("INSERT INTO Не успешно :(\n");
                            }
                            break;
                        }
                        case Float: {
                            try {
                                var t = String.valueOf(Float.valueOf(value[k].trim()));
                                //вставляем данные в поле
                                column.addRecord(t);
                            } catch (NumberFormatException e) {
                                check_error = true;
                                Platform.runLater(() ->
                                        main.error("Типы полей должны совпадать\nс типом данных"));
                                main.outText("INSERT INTO Не успешно :(\n");
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
                            main.outText("INSERT INTO Не успешно :(\n");
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
                    main.outText("INSERT INTO выполнилось успешно");
                });
            }
        } catch (NumberFormatException e) {
            Platform.runLater(() ->
                    main.error("Не удалось открыть таблицу"));
            main.outText("INSERT INTO Не успешно :(\n");
        }

    }

    void update(String request) throws IOException, ParserException {

        //берём данные команлды
        request = request.substring(request.toUpperCase().indexOf("UPDATE") + 6);

        //берём имя таблицы
        String table_name = request.substring(0, request.toUpperCase().indexOf("SET")).trim();

        //пары
        String[] pairs = request.substring(request.toUpperCase().indexOf("SET") + 3, request.toUpperCase().indexOf("WHERE")).trim().split("[,]");

        var name_pole = new String[pairs.length];
        for (int i = 0; i < pairs.length; i++) {
            name_pole[i] = pairs[i].substring(0, pairs[i].indexOf("=")).trim();
            pairs[i] = pairs[i].substring(pairs[0].toUpperCase().indexOf("=") + 1).trim();
        }

        //значения после where (логическое выражение)
        String logik = request.substring(request.toUpperCase().indexOf("WHERE") + 5, request.toUpperCase().indexOf(";")).trim();

        DataDbf dataDbf;

        try {
            //открываем выбранную таблицу
            var readerDbf = new ReaderDbf(table_name + ".dbf");
            dataDbf = readerDbf.read();
            //dataDbf = dataDbf.selectColumns(name_pole);
            readerDbf.close();
            //Настройка даты
            dataDbf.headerDbf.setData();

            Column[] columns = dataDbf.getAllColumns();

            var where = new Where();
            var toDbf = new DataDbf();

            //проход по списку пар
            for (var i = 0; i < pairs.length; i++) {
                //ищем совпадающее поле по имени в списке колонок
                for (Column column : columns) {
                    if (column.title.trim().equals(name_pole[i].trim())) {
                        //если поле совпало, то берём эту колонку и отправляем  в where
                        //для получени индексов значений, которые меняем
                        toDbf.setAllColumns(columns);
                        ArrayList<Integer> result = where.getRecs(logik, toDbf);

                        //тест
                        //main.outText(String.valueOf(result));

                        //проход внутри колонки по индексам из where и меняем каждое
                        for (Integer f : result) {
                            switch (column.type) {
                                case Integer: {
                                    try {
                                        column.data[f] = String.valueOf(Integer.valueOf(pairs[i].trim()));
                                    } catch (NumberFormatException e) {
                                        Platform.runLater(() -> main.outText("Не удалось провести\n одно из изменений\n"));
                                    }
                                    break;
                                }
                                case Float: {
                                    try {
                                        column.data[f] = String.valueOf(Float.valueOf(pairs[i].trim()));
                                    } catch (NumberFormatException e) {
                                        Platform.runLater(() -> main.outText("Не удалось провести \nодно из изменений\n"));
                                    }
                                    break;
                                }
                                case Character: {
                                    column.data[f] = pairs[i];
                                }
                                default: {
                                    Platform.runLater(() -> main.outText("Не удалось провести\n одно из изменений\n"));
                                    break;
                                }
                            }
                        }
                        //тест
                        //main.outText(column.title+"  "+column.data[0]+"  "+column.data[1]+"  "+column.data[2]+"  "+column.data[3]);
                        break;
                    }
                }
            }

            dataDbf.setAllColumns(columns);

            //сохраняем изменения
            var writerDbf = new WriterDbf(table_name + ".dbf");
            writerDbf.write(dataDbf);
            writerDbf.close();

            DataDbf finalDBF = dataDbf;
            //вывод изменений
            Platform.runLater(() -> {
                main.clearTable();
                main.setAllColumns(finalDBF.getColumnsforShow());
                main.outText("UPDATE выполнилось успешно");
            });
        } catch (NumberFormatException e) {
            Platform.runLater(() ->
                    main.error("Не удалось открыть таблицу"));
            main.outText("UPDATE Не успешно :(\n");
        }
    }

    void delete(String request) throws IOException, ParserException {
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
            main.outText("DELETE выполнилось успешно");
        });
    }

    void alterTable(String request) throws IOException, ParserException {
        int type = -50;
        int size_data = 0;
        TypesOfFields Type_Column = TypesOfFields.Integer;
        String Type_Action;
        String Name_Table;
        String Name_Column;
        String Type_Data;
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

            if (Type_Data.toUpperCase().compareTo("CHARACTER") == 0) {
                Type_Column = TypesOfFields.Character;
                size_data = Integer.parseInt(request.substring(request.indexOf("(") + 1, request.indexOf(")")).trim());
            }
            if (Type_Data.toUpperCase().compareTo("FLOAT") == 0) {
                Type_Column = TypesOfFields.Float;
                size_data = Integer.parseInt(request.substring(request.indexOf("(") + 1, request.indexOf(".")).trim());
                size_data+=Integer.parseInt(request.substring(request.indexOf(".")+1,request.indexOf(")")).trim());
                size_data+=1;
            }
            if (Type_Data.toUpperCase().compareTo("INTEGER") == 0) {
                Type_Column = TypesOfFields.Integer;
                size_data = Integer.parseInt(request.substring(request.indexOf("(") + 1, request.indexOf(")")).trim());
            }
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
                    Column[] buffer = new Column[length + 1];
                    System.arraycopy(columns, 0, buffer, 0, length);
                    String[] data = new String[dataDbf.recordsDbf.size()];
                    Column column = new Column(Type_Column, Name_Column, data, size_data);
                    buffer[length] = column;
                    dataDbf.setAllColumns(buffer);
                } else
                    Platform.runLater(() ->
                            main.error("Поле с таким именем уже\nсуществует"));
                break;
            }
            case 2: {
                var columns = new ArrayList<>(Arrays.asList(dataDbf.getAllColumns()));
                int check = 0;
                for (int i = 0; i < dataDbf.fieldsDbf.size(); i++) {
                    String namef = new String(dataDbf.fieldsDbf.get(i).getNameField());
                    namef = namef.trim();
                    if (Name_Column.compareTo(namef) == 0) {
                        check = 1;
                        Platform.runLater(() ->
                                main.outText("FIND"));
                        columns.remove(i);
                        dataDbf.setAllColumns(columns);
                        break;
                    } else
                        check = 2;
                }
                if (check == 2)
                    Platform.runLater(() ->
                            main.error("Не найдена колонка"));
                break;
            }
            case 3: {
                Platform.runLater(() ->
                        main.outText("MODIFY"));
                int check = 0;
                for (int i = 0; i < dataDbf.fieldsDbf.size(); i++) {
                    String namef = new String(dataDbf.fieldsDbf.get(i).getNameField());
                    namef = namef.trim();
                    if (Name_Column.compareTo(namef) == 0) {
                        check = 1;
                        Platform.runLater(() ->
                                main.outText("FIND"));
                        if((int)dataDbf.fieldsDbf.get(i).getSizeField() <= size_data) {//увеличиваем размер поля только в большую сторону
                            dataDbf.fieldsDbf.get(i).setSizeField((byte) size_data);
                            if(dataDbf.fieldsDbf.get(i).getTypeOfField() != Type_Column)
                            if(dataDbf.fieldsDbf.get(i).getTypeOfField() == TypesOfFields.Integer && Type_Column == TypesOfFields.Float ) {
                                dataDbf.fieldsDbf.get(i).setTypeField(Type_Column.code);
                                break;
                            }
                            else
                            {
                                throw new ParserException("Изменение типа возможно \nтолько из Integer в Float.");
                            }
                            break;
                        }
                        else
                        {
                            throw new ParserException("Размер поля можно увеличивать \nтолько в большую сторону.");
                        }

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
            main.outText("ALTER TABLE выполнилось успешно");
        });
    }

    void truncate(String request) throws IOException {
        String table_name;
        table_name = request.substring(request.indexOf(" TABLE ") + 6, request.indexOf(";")).trim();
        DataDbf dataDBF;
        ReaderDbf reader = new ReaderDbf(table_name + ".dbf");
        dataDBF = reader.read();
        reader.close();

        for (int i = 0; i < dataDBF.recordsDbf.size(); i++) {
            dataDBF.recordsDbf.remove(i);
        }

        dataDBF.headerDbf.setNumberOfRecords(0);
        setDBF(dataDBF, table_name);

        Platform.runLater(() -> {
            main.clearTable();
            main.setAllColumns(dataDBF.getColumnsforShow());
            main.outText("TRUNCATE выполнилось успешно");
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
            main.outText("DROP TABLE выполнилось успешно");
        });
    }

    void dropIndex(String request) throws IOException {
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

        File file = new File(indexName + ".idx");
        boolean delete = file.delete();

        Platform.runLater(()->main.outText("Удалено:"+delete));
    }

    private static void setDBF(DataDbf dataDBF, String table_name) throws IOException {
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
