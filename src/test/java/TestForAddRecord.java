import SQL.Lib.AdditionalInstruments.Column;
import SQL.Lib.Dbf.TypesOfFields;

class TestForAddRecord {

    public static void main(String[] args) {
        Column column=new Column(TypesOfFields.Integer,"lol",new String[]{"lol","vasya"},54);
        column.addRecord("OLOLOLOL");
    }
}
