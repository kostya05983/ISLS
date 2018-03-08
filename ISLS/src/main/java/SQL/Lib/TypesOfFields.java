package SQL.Lib;

public enum TypesOfFields {

    Integer('N',4),
    Float('F',4),
    Character('C',2);

public byte code;
public int size;


private TypesOfFields(char code,int size){
    this.code=(byte)code;
    this.size=size;
}

}
