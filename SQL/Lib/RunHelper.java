package SQL.Lib;

public class RunHelper {
    public static void main(String[] args) {
        byte[] array=new byte[32];



        HeaderDbf headerDbf=new HeaderDbf(array);

        String[] strArray=new String[]{
                "Лол","Кот","Кек"
        };
        Column[] columns=new Column[1];
        columns[0]=new Column(TypesOfFields.Character,"Бред",strArray);

        DataDbf dataDbf=new DataDbf(headerDbf);

        dataDbf.setAllColumns(columns);

        WriterDbf writerDbf=new WriterDbf("test.dbf");
        writerDbf.write(dataDbf);
    }
}
