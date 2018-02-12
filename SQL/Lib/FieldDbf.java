package SQL.Lib;

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
}
