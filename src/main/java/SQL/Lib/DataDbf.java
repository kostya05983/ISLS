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

    public DataDbf(){

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
            arrayList.add(new Column(fieldsDbf.get(i).getTypeOfField(),new String(fieldsDbf.get(i).getNameFiled()),table[i],fieldsDbf.get(i).getSizeField()));
        }

        Column[] columns=new Column[arrayList.size()];
        columns=arrayList.toArray(columns);

        return columns;
    }

    private String intitalizeNullString(int size){
        String result="";
        for(int i=0;i<size;i++){
            result+=(char)0;
        }
        return result;
    }

    public void setAllColumns(Column[] columns){//TODO  test
        if(!(fieldsDbf==null)) {
            fieldsDbf.clear();
            recordsDbf.clear();
        }
        if(fieldsDbf==null) {
            fieldsDbf=new ArrayList<>();
            recordsDbf=new ArrayList<>();
        }
        for(int i=0;i<columns.length;i++){
                for(int j=0;j<columns[i].data.length;j++)
                    if(columns[i].data[j]==null)
                columns[i].data[j]=intitalizeNullString(columns[i].size);
        }

        FieldDbf fieldDbf;
        String[][] buf=new String[columns.length][];
        for(int i=0;i<columns.length;i++){
            fieldDbf=new FieldDbf();
            fieldDbf.setNameField(columns[i].title);
            fieldDbf.setTypeField(columns[i].type.code);
            fieldDbf.setSizeField((byte)columns[i].size);//Размер поля в бинарном формате
            fieldDbf.setNumberOfCh((byte)i);
            fieldsDbf.add(fieldDbf);
            buf[i]=columns[i].data;
        }

        String tmp;
        buf=transportMatrix(buf);

        RecordDbf recordDbf;
        ByteBuffer byteBuffer;
        byte[] tmpByte;
        for(int i=0;i<buf.length;i++){//Пишем записи
            recordDbf=new RecordDbf();
            byteBuffer=ByteBuffer.allocate(sizeBuffer());
            for(int j=0;j<buf[i].length;j++){//Пишем запись
                tmpByte = buf[i][j].getBytes();
                for(int k=0;k<fieldsDbf.get(j).getSizeField();k++){//пишем одун ячейку
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

    public String[][] transportMatrix(String[][] a) {
        String[][] b = new String[a[0].length][a.length];

        for (int i = 0; i < a[0].length; i++)
            for (int j = 0; j < a.length; j++) {
                b[i][j] = a[j][i];
            }
        return b;
    }

}
