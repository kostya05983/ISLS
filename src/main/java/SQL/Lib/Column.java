package SQL.Lib;

public class Column {
    public TypesOfFields type;
    public String title;
    public String data[];

    public Column(TypesOfFields type,String title,String data[]){
        this.type=type;
        this.title=title;
        this.data=data;
    }
    public Column(String title){
        this.title=title;
    }
    protected int max(){
        int max=data[0].getBytes().length;
        for(int i=0;i<data.length;i++){
            if(max<data[i].length())
                max=data[i].getBytes().length;
        }
        return max;
    }
}