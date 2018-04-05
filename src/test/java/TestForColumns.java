import SQL.Lib.AdditionalInstruments.Column;
import SQL.Lib.DataHandler.ReaderDbf;
import SQL.Lib.DataHandler.WriterDbf;
import SQL.Lib.Dbf.DataDbf;
import SQL.Lib.Dbf.HeaderDbf;
import SQL.Lib.Dbf.TypesOfFields;

public class TestForColumns {
    public static void main(String[] args) {
        DataDbf dataDbf=new DataDbf();
        Column[] buf=new Column[2];

       // Column buf1=new Column();
        buf[0]=new Column();
        buf[0].size=5;
        buf[0].data=new String[2];
        buf[0].title=new String("lol");
        buf[0].type=TypesOfFields.Character;
        buf[1]=new Column();
        buf[1].size=5;
        buf[1].type=TypesOfFields.Character;
        buf[1].title=new String("lolo");
        buf[1].data=new String[]{"11111","22222"};

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

        dataDbf.headerDbf=headerDbf;

        dataDbf.setAllColumns(buf);

        WriterDbf writerDbf=new WriterDbf("test.dbf");

        writerDbf.write(dataDbf);
        writerDbf.close();

        ReaderDbf readerDbf=new ReaderDbf("test.dbf");

        DataDbf newdata=new DataDbf();

        dataDbf=readerDbf.read();

        Column[] columns=dataDbf.getAllColumns();

        readerDbf.close();

    }
}
