package SQL.Lib.Dbf;

public enum TypesOfFields {

    Integer('N'),
    Float('F'),
    Character('C');

    public byte code;

    TypesOfFields(char code)
    {
        this.code=(byte)code;
    }

}
