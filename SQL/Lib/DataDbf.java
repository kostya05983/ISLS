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

    public Column[] getAllColumns(){

        return null;
    }
    public void setAllColumns(Column[] columns){
        fieldsDbf.clear();
        recordsDbf.clear();
        FieldDbf fieldDbf=new FieldDbf();
        String[][] buf=new String[columns.length][];
        for(int i=0;i<columns.length;i++){
            fieldDbf.setNameField(columns[i].title);
            fieldDbf.setTypeField(columns[i].type.code);
            fieldDbf.setSizeField((byte)columns[i].data.length);//Размер поля в бинарном формате
            fieldDbf.setNumberOfCh((byte)i);
            fieldsDbf.add(fieldDbf);
            buf[i]=columns[i].data;
        }

        String tmp;
        for(int i=0;i<buf.length;i++)//Транспонируем матрицу
            for(int j=0;j<buf[i].length;j++) {
                tmp = buf[i][j];
                buf[i][j]=buf[j][i];
                buf[j][i]=tmp;
            }

        RecordDbf recordDbf=new RecordDbf();
        ByteBuffer byteBuffer=ByteBuffer.allocate(100000);
        byte[] tmpByte;
        for(int i=0;i<buf.length;i++){//Пишем записи
            for(int j=0;j<buf[i].length;j++){//Пишем запись
                tmpByte = buf[i][j].getBytes();
                for(int k=0;i<fieldsDbf.get(i).getSizeField();k++){//Пишем одну яейку записи
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


}
