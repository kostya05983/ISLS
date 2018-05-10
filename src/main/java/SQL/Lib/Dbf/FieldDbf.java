package SQL.Lib.Dbf;


public class FieldDbf {

    //region Parameters

    private byte[] nameField = new byte[11];//0-10 имя поля с 0x00 завершением
    private byte typeField;//Тип поля 11
    //зарезирвированная область 12-15
    private byte sizeField;//16 размер поля
    private byte numberOfCh;//17 Порядковый номер поля в бинарном формате
    //зарезирвированно два байта 0,0 18-19
    private byte identification;//Идентификатор рабочей области 20
    //21-30 зарезирвированная область
    private byte flagMdx;//Включено или нет поле в mdx индекс 31

    //endregion

    //region Constructors

    public FieldDbf() {

    }

    public FieldDbf(byte[] Array) {
        System.arraycopy(Array, 0, this.nameField, 0, 11);
        this.typeField = Array[11];
        this.sizeField = Array[16];
        this.numberOfCh = Array[17];
        this.identification = Array[20];
        this.flagMdx = Array[31];
    }

    //endregion

    //region InterfaceMethods

    public byte[] getByteCode() {
        byte[] result = new byte[32];

        System.arraycopy(this.nameField, 0, result, 0, this.nameField.length);

        result[11] = this.typeField;

        result[16] = this.sizeField;
        result[17] = this.numberOfCh;
        result[18] = 0;
        result[19] = 0;
        result[20] = this.identification;

        for (int i = 21; i < 31; i++)
            result[i] = 0;


        result[31] = flagMdx;

        return result;
    }

    public byte[] getNameField() {
        return nameField;
    }

    public void setNameField(String name) {
        char[] array = name.toCharArray();
        for (int i = 0; i < array.length; i++)
            nameField[i] = (byte) array[i];
    }

    public void setTypeField(byte typeField) {
        this.typeField = typeField;
    }

    public byte getSizeField() {
        return sizeField;
    }

    public void setSizeField(byte sizeField) {
        this.sizeField = sizeField;
    }

    public void setNumberOfCh(byte numberOfCh) {
        this.numberOfCh = numberOfCh;
    }

    public void setIdentification(byte identification) {
        this.identification = identification;
    }

    public void setFlagMdx(byte flagMdx) {
        this.flagMdx = flagMdx;
    }

    public TypesOfFields getTypeOfField() {
        switch (typeField) {
            case 'C':
                return TypesOfFields.Character;
            case 'F':
                return TypesOfFields.Float;
            case 'N':
                return TypesOfFields.Integer;

            default:
                return null;
        }

    }

    //endregion

}
