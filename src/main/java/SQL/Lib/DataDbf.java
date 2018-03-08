package SQL.Lib;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DataDbf {

    public HeaderDbf headerDbf;
    public ArrayList<FieldDbf> fieldsDbf;
    public ArrayList<RecordDbf> recordsDbf;




    public DataDbf(HeaderDbf headerDbf, ArrayList<FieldDbf> fieldDbf, ArrayList<RecordDbf> recordDbf){
        this.headerDbf=headerDbf;
        this.fieldsDbf=fieldDbf;
        this.recordsDbf=recordDbf;
    }

    public DataDbf(HeaderDbf headerDbf){
        this.headerDbf=headerDbf;
        this.fieldsDbf=new ArrayList<>();
        this.recordsDbf=new ArrayList<>();
    }

    public DataDbf(HeaderDbf headerDbf,ArrayList<FieldDbf> fieldDbf){
        this.headerDbf=headerDbf;
        this.fieldsDbf=fieldDbf;
    }
    private short transferByteToUnsigned(byte b){
        if(b<=0){
            return (short)(127+(128+b));
        }
        return b;
    }
    public Column[] getAllColumns(){
        ArrayList<Column> arrayList=new ArrayList<>();
        String[][] table=new String[fieldsDbf.size()][headerDbf.getNumberOfRecords()];
        int start;

        for(int i=0;i<recordsDbf.size();i++) {
            start=0;
            for (int j = 0; j < fieldsDbf.size(); j++) {
                table[j][i] = recordsDbf.get(i).getPartOfRecord(start, transferByteToUnsigned(fieldsDbf.get(j).getSizeField()));
                start += transferByteToUnsigned(fieldsDbf.get(j).getSizeField());
            }
        }
        for(int i=0;i<fieldsDbf.size();i++){
            arrayList.add(new Column(fieldsDbf.get(i).getTypeOfField(),new String(fieldsDbf.get(i).getNameFiled()),table[i]));
        }

        Column[] columns=new Column[arrayList.size()];
        columns=arrayList.toArray(columns);

        return columns;
    }



    public void setAllColumns(Column[] columns){//TODO рефактор test
        if(!(fieldsDbf==null)) {
            fieldsDbf.clear();
            recordsDbf.clear();
        }

        FieldDbf fieldDbf=new FieldDbf();
        String[][] buf=new String[columns.length][];
        for(int i=0;i<columns.length;i++){
            fieldDbf.setNameField(columns[i].title);
            fieldDbf.setTypeField(columns[i].type.code);
            fieldDbf.setSizeField((byte)columns[i].max());//Размер поля в бинарном формате
            fieldDbf.setNumberOfCh((byte)i);
            fieldsDbf.add(fieldDbf);
            buf[i]=columns[i].data;
        }

        String tmp;
        buf=transportMatrix(buf);

        RecordDbf recordDbf=new RecordDbf();
        ByteBuffer byteBuffer;
        byte[] tmpByte;
        for(int i=0;i<buf.length;i++){//Пишем записи
            byteBuffer=ByteBuffer.allocate(sizeBuffer());
            for(int j=0;j<buf[i].length;j++){//Пишем запись
                tmpByte = buf[i][j].getBytes();
                System.out.println(fieldsDbf.get(i).getSizeField());
                for(int k=0;k<fieldsDbf.get(i).getSizeField();k++){//пишем одун ячейку
                    if(k<tmpByte.length)
                        byteBuffer.put(tmpByte[k]);
                    else
                        byteBuffer.put((byte)0);
                }
            }
            recordDbf.setByteCode((byte)' ',byteBuffer.array());
            byteBuffer.clear();
            recordsDbf.add(recordDbf);
        }

    }

    private int sizeBuffer(){
        int sum=0;

        for(int i=0;i<fieldsDbf.size();i++){
            sum+=fieldsDbf.get(i).getSizeField();
        }

        return sum;
    }

    private String[][] transportMatrix(String[][] a) {
        String[][] b = new String[a.length][a.length];
        for (int i = 0; i < a.length; ++i)
            for (int j = 0; j < a.length; ++j) {
                b[i][j] = a[j][i];
            }
        return b;
    }

}
