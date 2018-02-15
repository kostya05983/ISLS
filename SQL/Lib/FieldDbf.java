package SQL.Lib;

import java.nio.ByteBuffer;

public class FieldDbf {
    private byte[] nameFiled=new byte[11];//0-10 имя поля с 0x00 завершением
    private byte typeField;//Тип поля 11
    //зарезирвированная область 12-15
    private byte sizeField;//16 размер поля
    private byte numberOfCh;//17 Порядковый номер поля в бинарном формате
    //зарезирвированно два байта 0,0 18-19
    private byte identificator;//Идентификатор рабочей области 20
    //21-30 зарезирвированная область
    private byte flagMdx;//Включено или нет поле в mdx индекс 31

    //Конструктор Виталина
    public FieldDbf(byte[] Array)
    {
        System.arraycopy(Array,0,this.nameFiled,0,11);
        this.typeField=Array[11];
        this.sizeField=Array[16];
        this.numberOfCh=Array[17];
        this.identificator=Array[20];
        this.flagMdx=Array[31];
    }
    //Метод гетер всех байтов
    public byte[] getByteCode(){
        byte [] result=new byte[32];

        for(int i=0;i<this.nameFiled.length;i++){
            result[i]=this.nameFiled[i];
        }
        result[11]=typeField;


        result[16]=this.sizeField;
        result[17]=this.numberOfCh;
        result[18]=0;
        result[19]=0;
        result[20]=this.identificator;

        for(int i=21;i<31;i++){
            result[i]=0;
        }

        result[31]=flagMdx;

        return result;
    }

    public void setNameFiled(byte[] nameFiled) {
        this.nameFiled = nameFiled;
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

    public byte[] getNameFiled() {
        return nameFiled;
    }

    public byte getTypeField() {
        return typeField;
    }

    public byte getSizeField() {
        return sizeField;
    }

    public byte getNumberOfCh() {
        return numberOfCh;
    }

    public byte getIdentificator() {
        return identificator;
    }

    public byte getFlagMdx() {
        return flagMdx;
    }
}