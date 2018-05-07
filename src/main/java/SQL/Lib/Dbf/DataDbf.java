package SQL.Lib.Dbf;

import SQL.Lib.AdditionalInstruments.Column;
import SQL.Parser.ParserException;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DataDbf {

    //region Properties

    public HeaderDbf headerDbf;
    public ArrayList<FieldDbf> fieldsDbf;
    public ArrayList<RecordDbf> recordsDbf;

    //endregion

    //region Constructors

    public DataDbf(HeaderDbf headerDbf, ArrayList<FieldDbf> fieldDbf, ArrayList<RecordDbf> recordDbf) {
        this.headerDbf = headerDbf;
        this.fieldsDbf = fieldDbf;
        this.recordsDbf = recordDbf;
    }

    public DataDbf(HeaderDbf headerDbf) {
        this.headerDbf = headerDbf;
        this.fieldsDbf = new ArrayList<>();
        this.recordsDbf = new ArrayList<>();
    }

    public DataDbf(HeaderDbf headerDbf, ArrayList<FieldDbf> fieldDbf) {
        this.headerDbf = headerDbf;
        this.fieldsDbf = fieldDbf;
    }

    public DataDbf() {

    }

    //endregion

    //region InterfaceMethods

    public Column[] getAllColumns() {
        ArrayList<Column> arrayList = new ArrayList<>();
        String[][] table = new String[fieldsDbf.size()][headerDbf.getNumberOfRecords()];
        int start;

        if (recordsDbf != null) {
            for (int i = 0; i < recordsDbf.size(); i++) {
                start = 0;
                for (int j = 0; j < fieldsDbf.size(); j++) {
                    table[j][i] = recordsDbf.get(i).getPartOfRecord(start, transferByteToUnsigned(fieldsDbf.get(j).getSizeField()));
                    start += transferByteToUnsigned(fieldsDbf.get(j).getSizeField());
                }
            }
            for (int i = 0; i < fieldsDbf.size(); i++)
                arrayList.add(new Column(fieldsDbf.get(i).getTypeOfField(), getPartOfRecord(fieldsDbf.get(i).getNameField()), table[i], transferByteToUnsigned(fieldsDbf.get(i).getSizeField())));

        } else {
            for (FieldDbf aFieldsDbf : fieldsDbf)
                arrayList.add(new Column(aFieldsDbf.getTypeOfField(), getPartOfRecord(aFieldsDbf.getNameField()), transferByteToUnsigned(aFieldsDbf.getSizeField())));

        }

        Column[] columns = new Column[arrayList.size()];
        columns = arrayList.toArray(columns);

        return columns;
    }

    public Column[] getColumnsforShow() {
        ArrayList<Column> arrayList = new ArrayList<>();
        if (recordsDbf != null) {
            String[][] table = new String[fieldsDbf.size()][headerDbf.getNumberOfRecords()];
            int start;

            for (int i = 0; i < recordsDbf.size(); i++) {
                start = 0;
                if (recordsDbf.get(i).getHeaderByte() != 42)
                    for (int j = 0; j < fieldsDbf.size(); j++) {
                        table[j][i] = recordsDbf.get(i).getPartOfRecord(start, transferByteToUnsigned(fieldsDbf.get(j).getSizeField()));
                        start += transferByteToUnsigned(fieldsDbf.get(j).getSizeField());
                    }
            }
            for (int i = 0; i < fieldsDbf.size(); i++)
                arrayList.add(new Column(fieldsDbf.get(i).getTypeOfField(), getPartOfRecord(fieldsDbf.get(i).getNameField()), table[i], transferByteToUnsigned(fieldsDbf.get(i).getSizeField())));

        } else {
            for (FieldDbf aFieldsDbf : fieldsDbf)
                arrayList.add(new Column(aFieldsDbf.getTypeOfField(), getPartOfRecord(aFieldsDbf.getNameField()), transferByteToUnsigned(aFieldsDbf.getSizeField())));

        }

        Column[] columns = new Column[arrayList.size()];
        columns = arrayList.toArray(columns);

        return columns;
    }

    public DataDbf selectColumns(String[] names) throws ParserException {
        Column[] columns = getAllColumns();
        ArrayList<Column> resultColumns = new ArrayList<>();
        boolean flag;

        for (String name : names) {
            flag = false;
            for (Column column : columns) {
                if (column.title.equals(name)) {
                    resultColumns.add(column);
                    flag = true;
                    break;
                }
            }
            if (!flag)
                throw new ParserException("Имя поля не найдено,проверьте имена");
        }

        DataDbf result = new DataDbf(headerDbf, fieldsDbf, recordsDbf);
        Column[] resultArrColumns = new Column[resultColumns.size()];
        resultColumns.toArray(resultArrColumns);
        result.setAllColumns(resultArrColumns);

        return result;
    }

    public void setAllColumns(Column[] columns) {
        if (!(fieldsDbf == null)) {
            fieldsDbf.clear();
            recordsDbf.clear();
        }
        if (fieldsDbf == null) {
            fieldsDbf = new ArrayList<>();
            recordsDbf = new ArrayList<>();
        }

        short lentgthRecord = 0;
        for (Column column : columns) {
            for (int j = 0; j < column.data.length; j++) {
                lentgthRecord += column.size;
                if (column.data[j] == null)
                    column.data[j] = intitalizeNullString(column.size);
            }
        }
        if (headerDbf == null)
            headerDbf = new HeaderDbf();

        headerDbf.setLengthOfRecord(lentgthRecord);
        headerDbf.setLengthOfTitle((short) (columns.length * 32));
        headerDbf.setNumberOfRecords(columns[0].data.length);


        FieldDbf fieldDbf;
        String[][] buf = new String[columns.length][];
        for (int i = 0; i < columns.length; i++) {
            fieldDbf = new FieldDbf();
            fieldDbf.setNameField(columns[i].title);
            fieldDbf.setTypeField(columns[i].type.code);
            fieldDbf.setSizeField((byte) columns[i].size);//Размер поля в бинарном формате
            fieldDbf.setNumberOfCh((byte) i);
            fieldsDbf.add(fieldDbf);
            buf[i] = columns[i].data;
        }

        buf = transportMatrix(buf);

        writeRecords(buf);

    }

    public void setAllColumns(ArrayList<Column> columns) {
        if (!(fieldsDbf == null)) {
            fieldsDbf.clear();
            recordsDbf.clear();
        }
        if (fieldsDbf == null) {
            fieldsDbf = new ArrayList<>();
            recordsDbf = new ArrayList<>();
        }

        short lentgthRecord = 0;
        for (Column column : columns) {
            for (int j = 0; j < column.data.length; j++) {
                lentgthRecord += column.size;
                if (column.data[j] == null)
                    column.data[j] = intitalizeNullString(column.size);
            }
        }
        if (headerDbf == null)
            headerDbf = new HeaderDbf();

        headerDbf.setLengthOfRecord(lentgthRecord);
        headerDbf.setLengthOfTitle((short) (columns.size() * 32));
        headerDbf.setNumberOfRecords(columns.get(0).data.length);


        FieldDbf fieldDbf;
        String[][] buf = new String[columns.size()][];
        for (int i = 0; i < columns.size(); i++) {
            fieldDbf = new FieldDbf();
            fieldDbf.setNameField(columns.get(i).title);
            fieldDbf.setTypeField(columns.get(i).type.code);
            fieldDbf.setSizeField((byte) columns.get(i).size);//Размер поля в бинарном формате
            fieldDbf.setNumberOfCh((byte) i);
            fieldsDbf.add(fieldDbf);
            buf[i] = columns.get(i).data;
        }

        buf = transportMatrix(buf);

        writeRecords(buf);
    }


    public String[][] transportMatrix(String[][] a) {
        String[][] b = new String[a[0].length][a.length];

        for (int i = 0; i < a[0].length; i++)
            for (int j = 0; j < a.length; j++) {
                b[i][j] = a[j][i];
            }
        return b;
    }

    public int[] getPositions(String field) {
        int positionH = 0;
        int[] positions = new int[recordsDbf.size()];
        for (FieldDbf aFieldsDbf : fieldsDbf) {
            positionH += aFieldsDbf.getSizeField();
            if (getPartOfRecord(aFieldsDbf.getNameField()).equals(field)) {
                for (int j = 0; j < positions.length; j++) {
                    positions[j] = (headerDbf.getLengthOfTitle() + positionH + headerDbf.getLengthOfRecord() * j);
                }
                return positions;
            }
        }
        return null;
    }

    public String getPartOfRecord(byte[] data) {
        int i;
        for (i = 0; i < data.length; i++) {
            if (data[i] == 0)
                break;
        }
        byte[] result = new byte[i];
        System.arraycopy(data, 0, result, 0, i);

        return new String(result);
    }

    //endregion

    //region PrivateMethods

    private short transferByteToUnsigned(byte b) {
        if (b <= 0) {
            return (short) (256 + b);
        }
        return b;
    }

//    private byte transferIntToByte(){
//
//    }

    private int sizeBuffer() {
        int sum = 0;

        for (FieldDbf aFieldsDbf : fieldsDbf) {
            sum += transferByteToUnsigned(aFieldsDbf.getSizeField());
        }

        return sum;
    }

    private String intitalizeNullString(int size) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            result.append((char) 0);
        }
        return result.toString();
    }

    private void writeRecords(String[][] buf) {
        RecordDbf recordDbf;
        ByteBuffer byteBuffer;
        byte[] tmpByte;
        for (String[] aBuf : buf) {//Пишем записи
            recordDbf = new RecordDbf();
            byteBuffer = ByteBuffer.allocate(sizeBuffer());
            for (int j = 0; j < aBuf.length; j++) {//Пишем запись
                tmpByte = aBuf[j].getBytes();
                for (int k = 0; k < transferByteToUnsigned(fieldsDbf.get(j).getSizeField()); k++) {//пишем одну ячейку
                    if (k < tmpByte.length)
                        byteBuffer.put(tmpByte[k]);
                    else
                        byteBuffer.put((byte) 0);
                }
            }
            recordDbf.setByteCode((byte) ' ', byteBuffer.array());
            byteBuffer.clear();
            recordsDbf.add(recordDbf);
        }
    }

    //endregion

}
