import SQL.Lib.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class FirstTestForLib {

    public static void main(String[] args) {
        //Заголовок //TODO Проверить конструктор лишний байт на multiUse
        HeaderDbf headerDbf=new HeaderDbf();
        headerDbf.setSignature((byte)2);
        headerDbf.setYear((byte)18);
        headerDbf.setMonth((byte)3);
        headerDbf.setDay((byte)8);
        headerDbf.setNumberOfRecords((byte)2);
        headerDbf.setLengthOfTitle((short)96);
        headerDbf.setLengthOfRecord((short)14);
        headerDbf.setFlagTransaction((byte)0);
        headerDbf.setFlagEncryption((byte)1);
        headerDbf.setFlagMDX((byte)2);
        headerDbf.setNumberOfDriver((byte)1);


        //Поля TODO Ошибка в записи,записи почему-то одинаковые??
        ArrayList<FieldDbf> arrayList=new ArrayList<>();

        FieldDbf fieldDbf=new FieldDbf();

        fieldDbf.setNameField("Дрова");
        fieldDbf.setTypeField(TypesOfFields.Character.code);
        fieldDbf.setSizeField((byte)10);
        fieldDbf.setNumberOfCh((byte)0);
        fieldDbf.setIdentificator((byte)1);
        fieldDbf.setFlagMdx((byte)0);

        arrayList.add(fieldDbf);

        fieldDbf.setNameField("Количество");
        fieldDbf.setTypeField(TypesOfFields.Integer.code);
        fieldDbf.setSizeField((byte)4);
        fieldDbf.setNumberOfCh((byte)1);
        fieldDbf.setIdentificator((byte)1);
        fieldDbf.setFlagMdx((byte)0);

        arrayList.add(fieldDbf);

        ArrayList<RecordDbf> recordDbfs=new ArrayList<>();
        byte[] data=new byte[14];
        ByteBuffer byteBuffer=ByteBuffer.allocate(100);
        byteBuffer.putChar('Б');
        byteBuffer.putChar('е');
        byteBuffer.putChar('р');
        byteBuffer.putChar('е');
        byteBuffer.putChar('з');
        //byteBuffer.putChar('а');

        for(int i=0;i<5;i++)
            data[i]=byteBuffer.get(i);

        data[9]=3;

        RecordDbf recordDbf=new RecordDbf();
        recordDbf.setHeaderByte((byte)'*');
        recordDbf.setData(data);

        recordDbfs.add(recordDbf);

       byteBuffer.clear();
        byteBuffer.putChar('С');
        byteBuffer.putChar('о');
        byteBuffer.putChar('с');
        byteBuffer.putChar('н');
        byteBuffer.putChar('а');


        for(int i=0;i<5;i++)
            data[i]=byteBuffer.get(i);

        data[9]=5;

        RecordDbf recordDb;
        recordDbf.setHeaderByte((byte)'*');
        recordDbf.setData(data);

        recordDbfs.add(recordDbf);

        DataDbf dataDbf=new DataDbf(headerDbf,arrayList,recordDbfs);

        WriterDbf writerDbf=new WriterDbf("test.dbf");
        writerDbf.write(dataDbf);

        ReaderDbf readerDbf=new ReaderDbf("test.dbf");

        dataDbf=readerDbf.read();

        System.out.println("eee");

    }
}
