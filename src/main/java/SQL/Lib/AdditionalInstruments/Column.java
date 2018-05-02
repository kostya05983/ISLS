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

    public Column(){

    }

    public Column(TypesOfFields type,String title,String data[],int size){
        this.type=type;
        this.title=title;
        this.data=data;
        this.size=size;
    }

    public Column(TypesOfFields type,String title){
        this.type=type;
        this.title=title;
        this.data=new String[0];
        this.size=0;
    }

    public Column(TypesOfFields type,String title,int size){
        this.type=type;
        this.title=title;
        this.size=size;
    }

    public Column(String title){
        this.title=title;
    }

    //endregion

    //region InterfaceMethods

    protected int max(){
        int max=data[0].getBytes().length;
        for (String aData : data) {
            if (max < aData.length())
                max = aData.getBytes().length;
        }
        return max;
    }

    public void addRecord(String record){
        data=new String[data.length+1];
        data[data.length-1]=record;
        size+=record.length();
    }

    //endregion

}
