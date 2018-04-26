package SQL.Lib.Dbf;


public class FieldDbf {

    //region Parameters

    private byte[] nameField=new byte[11];//0-10 имя поля с 0x00 завершением
    private byte typeField;//Тип поля 11
    //зарезирвированная область 12-15
    private byte sizeField;//16 размер поля
    private byte numberOfCh;//17 Порядковый номер поля в бинарном формате
    //зарезирвированно два байта 0,0 18-19
    private byte identificator;//Идентификатор рабочей области 20
    //21-30 зарезирвированная область
    private byte flagMdx;//Включено или нет поле в mdx индекс 31

    //endregion

    //region Constructors

    public FieldDbf(){

    }

    public FieldDbf(byte[] Array) {
        System.arraycopy(Array,0,this.nameField,0,11);
        this.typeField=Array[11];
        this.sizeField=Array[16];
        this.numberOfCh=Array[17];
        this.identificator=Array[20];
        this.flagMdx=Array[31];
    }

    //endregion

    //region InterfaceMethods

    public byte[] getByteCode(){
        byte [] result=new byte[32];

        System.arraycopy(this.nameField, 0, result, 0, this.nameField.length);

        result[11]=this.typeField;

        result[16]=this.sizeField;
        result[17]=this.numberOfCh;
        result[18]=0;
        result[19]=0;
        result[20]=this.identificator;

        for(int i=21;i<31;i++)
            result[i]=0;


        result[31]=flagMdx;

        return result;
    }

    public void setByteCode(byte[] array){
        System.arraycopy(array,0,this.nameField,0,11);
        this.typeField=array[11];
        this.sizeField=array[16];
        this.numberOfCh=array[17];
        this.identificator=array[20];
        this.flagMdx=array[31];
    }

    public void setNameField(byte[] nameFiled) {
        this.nameField = nameFiled;
    }

    public void setNameField(String name){
        char[] array=name.toCharArray();
        for(int i=0;i<array.length;i++)
            nameField[i]=(byte)array[i];
    }

    public void setTypeField(byte typeField) {
        this.typeField = typeField;
    }

    public void setSizeField(byte sizeField) {
        this.sizeField = sizeField;
    }

    public void setNumberOfCh(byte numberOfCh) {
        this.numberOfCh = numberOfCh;
    }

    public void setIdentificator(byte identificator) {
        this.identificator = identificator;
    }

    public void setFlagMdx(byte flagMdx) {
        this.flagMdx = flagMdx;
    }

    public byte[] getNameField() {
        return nameField;
    }

    public byte getTypeField() {
        return typeField;
    }

    public byte getSizeField() { return sizeField; }

    public byte getNumberOfCh() {
        return numberOfCh;
    }

    public byte getIdentificator() {
        return identificator;
    }

    public byte getFlagMdx() {
        return flagMdx;
    }

    TypesOfFields getTypeOfField(){
        switch(typeField){
            case 'C':return TypesOfFields.Character;
            case 'F':return TypesOfFields.Float;
            case 'N':return TypesOfFields.Integer;

            default:return null;
        }

   }

   //endregion

}
