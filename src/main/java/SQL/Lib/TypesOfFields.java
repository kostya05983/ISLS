package SQL.Lib;

public enum TypesOfFields {

    Integer('N'),
    Float('F'),
    Character('C');

    public byte code;

    private TypesOfFields(char code)
    {
        this.code=(byte)code;
    }

}
