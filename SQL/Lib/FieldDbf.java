package SQL.Lib;

import java.nio.ByteBuffer;

public class FieldDbf {
    private byte[] nameFiled=new byte[11];//0-10 имя поля с 0x00 завершением
    private byte typeField;//Тип поля 11
    private int adress;//Адрес поля в памяти 12-15
    private byte sizeField;//16 размер поля
    private byte numberOfCh;//17 колличество знаков после запятой
    //зарезирвированно два байта 0,0 18-19
    private byte identificator;//Идентификатор рабочей области 20
    private byte[] multiUser=new byte[2];//многопользовательский dBase n,n 21-22
    private byte establishedFields;//Установленые поля 23
    //Зарезирвированно 24-30
    private byte flagMdx;//Включено или нет поле в mdx индекс

    //Метод гетер всех байтов

    protected byte[] getByteCode(){
        byte [] result=new byte[32];

        for(int i=0;i<this.nameFiled.length;i++){
            result[i]=this.nameFiled[i];
        }
        result[11]=typeField;

        ByteBuffer byteBuffer=ByteBuffer.allocate(4);
        byteBuffer.putInt(this.adress);

        for (int i=0;i<4;i++){
            result[i+11]=byteBuffer.get(i);
        }
        result[16]=this.sizeField;
        result[17]=this.numberOfCh;
        result[18]=0;
        result[19]=0;
        result[20]=this.identificator;
        result[21]=this.multiUser[0];
        result[22]=this.multiUser[1];
        result[23]=this.establishedFields;

        for(int i=24;i<31;i++){
            result[i]=0;
        }
        result[31]=flagMdx;

        return result;
    }
    //Конструктор для установки полей
}
