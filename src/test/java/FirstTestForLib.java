import SQL.Lib.AdditionalInstruments.Column;
import SQL.Lib.DataHandler.ReaderDbf;
import SQL.Lib.DataHandler.WriterDbf;
import SQL.Lib.Dbf.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

class FirstTestForLib {

    public static void main(String[] args) throws IOException {
        //Заголовок
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


        //Поля
        ArrayList<FieldDbf> arrayList=new ArrayList<>();

        FieldDbf fieldDbf=new FieldDbf();

        fieldDbf.setNameField("Drova");
        fieldDbf.setTypeField(TypesOfFields.Character.code);
        fieldDbf.setSizeField((byte)10);
        fieldDbf.setNumberOfCh((byte)0);
        fieldDbf.setIdentification((byte)1);
        fieldDbf.setFlagMdx((byte)0);

        arrayList.add(fieldDbf);

        FieldDbf fieldDbf1=new FieldDbf();
        fieldDbf1.setNameField("Amount");
        fieldDbf1.setTypeField(TypesOfFields.Integer.code);
        fieldDbf1.setSizeField((byte)4);
        fieldDbf1.setNumberOfCh((byte)1);
        fieldDbf1.setIdentification((byte)1);
        fieldDbf1.setFlagMdx((byte)0);

        arrayList.add(fieldDbf1);

        ArrayList<RecordDbf> recordDbfs=new ArrayList<>();
        byte[] data=new byte[14];
       ByteBuffer byteBuffer=ByteBuffer.allocate(100);

       data[0]=(byte)'B';
       data[1]=(byte)'E';
       data[2]=(byte)'R';
       data[3]=(byte)'E';

        System.out.println((byte)'B');


        //data[9]=3;
        for(int i=0;i<4;i++){
            data[i+10]=(byte)'2';
        }

        RecordDbf recordDbf=new RecordDbf();
        recordDbf.setHeaderByte((byte)'*');
        recordDbf.setData(data);

        recordDbfs.add(recordDbf);

       byteBuffer.clear();


        byte[] datanew=new byte[14];
        datanew[0]=(byte)'S';
        datanew[1]=(byte)'o';
        datanew[2]=(byte)'s';
        datanew[3]=(byte)'n';


        for(int i=0;i<4;i++){
            datanew[i+10]=(byte)'1';
        }

        RecordDbf recordDb=new RecordDbf();
        recordDb.setHeaderByte((byte)'*');
        recordDb.setData(datanew);

        recordDbfs.add(recordDb);

        DataDbf dataDbf=new DataDbf(headerDbf,arrayList,recordDbfs);

        WriterDbf writerDbf=new WriterDbf("test.dbf");
        writerDbf.write(dataDbf);

        ReaderDbf readerDbf=new ReaderDbf("test.dbf");

        dataDbf=readerDbf.read();

        Column[] coulmns=dataDbf.getAllColumns();

        System.out.println(coulmns[0].data[0]);
        System.out.println(coulmns[0].data[1]);
        System.out.println(coulmns[1].data[0]);
        System.out.println(coulmns[1].data[1]);


        System.out.println("eee");

    }
}
