import SQL.Lib.*;

public class Example {

    public static void main(String[] args) {
        String str="DELETE FROM Customers\n" +
                "WHERE CustomerName='Alfreds Futterkiste';";

        String str1="UPDATE Customers\n" +
                "SET ContactName = 'Alfred Schmidt', City= 'Frankfurt'\n" +
                "WHERE CustomerID = 1;";

        String name="test";

        ReaderDbf readerDbf=new ReaderDbf(name+".dbf");

        DataDbf dataDbf=new DataDbf();
        readerDbf.close();
        dataDbf=readerDbf.read(dataDbf);


        dataDbf.headerDbf.setDay((byte)10);
        dataDbf.headerDbf.setMonth((byte)3);
        dataDbf.headerDbf.setYear((byte)18);


        Column[] columns=dataDbf.getAllColumns();
        String data1=columns[0].data[0];
        System.out.println(data1);



        dataDbf.setAllColumns(columns);

//        HeaderDbf headerDbf;
//        headerDbf=dataDbf.headerDbf;



        WriterDbf writerDbf=new WriterDbf(name+".dbf");
        writerDbf.write(dataDbf);
        writerDbf.close();



        //dataDbf.
    }
}
