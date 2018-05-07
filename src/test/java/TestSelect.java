import SQL.Lib.AdditionalInstruments.Column;
import SQL.Lib.DataHandler.WriterDbf;
import SQL.Lib.Dbf.DataDbf;
import SQL.Lib.Dbf.HeaderDbf;
import SQL.Lib.Dbf.TypesOfFields;
import SQL.Parser.SelectorRequest;

import java.io.IOException;

class TestSelect {

    public static void main(String[] args) throws IOException {
        //Заголовок
        HeaderDbf headerDbf=new HeaderDbf();
        headerDbf.setSignature((byte)2);
        headerDbf.setYear((byte)18);
        headerDbf.setMonth((byte)3);
        headerDbf.setDay((byte)8);
        headerDbf.setNumberOfRecords((byte)3);
        headerDbf.setLengthOfTitle((short)96);
        headerDbf.setLengthOfRecord((short)14);
        headerDbf.setFlagTransaction((byte)0);
        headerDbf.setFlagEncryption((byte)1);
        headerDbf.setFlagMDX((byte)2);
        headerDbf.setNumberOfDriver((byte)1);

        String[] data1=new String[]{
                "лол",
                "лоло",
                "ололо"

        };
        String[] data2=new String[]{
                "23",
                "34",
                "45",
        };
        String[] data3=new String[]{
                "23",
                "34",
                "45",
        };

        Column column=new Column(TypesOfFields.Character,"lol",data1,12);
        Column column1=new Column(TypesOfFields.Integer,"olol",data2,6);
        Column column2=new Column(TypesOfFields.Float,"rororo",data3,6);

        Column[] columns=new Column[]{
                column,
                column1,
                column2
        };
        DataDbf dataDbf=new DataDbf(headerDbf);
        dataDbf.setAllColumns(columns);

        WriterDbf writerDbf=new WriterDbf("lol.dbf");
        writerDbf.write(dataDbf);
        writerDbf.close();


        SelectorRequest selectorRequest=new SelectorRequest("SELECT * FROM lol  + WHERE olol=23 OR olol=34;",null);
        selectorRequest.run();

    }
}
