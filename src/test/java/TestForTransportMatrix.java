import SQL.Lib.Dbf.DataDbf;

class TestForTransportMatrix {

    public static void main(String[] args) {
        DataDbf dataDbf=new DataDbf();

        String[][] str={
                new String[]{"lol","lol1"},
                new String[]{"lol2","lol3"},
                new String[]{"lol4","lol5"}
        };
        String[][] str1={
                new String[]{"lol1","lol2","lol3"},
                new String[]{"lol4","lol5","lol6"}
        };
        String[][] str2={
                new String[]{"lol","lol1"},
                new String[]{"lol2","lol3"}
        };

        dataDbf.transportMatrix(str1);
        dataDbf.transportMatrix(str);
        dataDbf.transportMatrix(str2);
    }
}
