package SQL.Lib.AdditionalInstruments;

import SQL.Lib.Dbf.TypesOfFields;

public class Column {

    //region Properties

    public TypesOfFields type;
    public String title;
    public String data[];
    public int size;

    //endregion

    //region Constructors

    public Column() {

    }

    public Column(TypesOfFields type, String title, String data[], int size) {
        this.type = type;
        this.title = title;
        this.data = data;
        this.size = size;
    }

    public Column(TypesOfFields type, String title, int size) {
        this.type = type;
        this.title = title;
        this.size = size;
    }

    //endregion

    //region InterfaceMethods

    public void addRecord(String record) {
        var buffer = data;
        data = new String[data.length + 1];

        System.arraycopy(buffer, 0, data, 0, data.length - 1);
        data[data.length - 1] = record;
        size += record.length();
    }

    //endregion

}
