package SQL.Lib;

import java.nio.ByteBuffer;
import java.util.Date;

public class HeaderDbf {
    private byte signature;//0
    private byte year;//1
    private byte month;//2
    private byte day;//3
    private int numberOfRecords;// число записей//4-7
    private short lengthOfTitle;//размер заголовка в байтах//8-9
    private short lengthOfRecord;//длина одной записи//10-11
    //12-13 зарезирвированно
    private byte flagTransaction;//флагн на наличие незавершенной транзакции 14
    private byte flagEncryption;//флаг шифрования таблицы 15
    private byte[] fieldMultiUserUse=new byte[13];//зарезирвированная область для многопользовательского использовапния 16-27
    private byte flagMDX;//флаг наличия индексного MDX- файла 28
    private byte NumberOfDriver;//29 Идентификатор кодовой страницы файла 29
    //30-31 зарезирвированная область


    public HeaderDbf(byte[] ArrayOfHeader)
    {
        this.signature=ArrayOfHeader[0];
        this.year=ArrayOfHeader[1];
        this.month=ArrayOfHeader[2];
        this.day=ArrayOfHeader[3];
        ByteBuffer wrapped=ByteBuffer.wrap(ArrayOfHeader,4,4);
        this.numberOfRecords=wrapped.getInt();
        wrapped.clear();
        wrapped=ByteBuffer.wrap(ArrayOfHeader,8,2);
        this.lengthOfTitle=wrapped.getShort();
        wrapped.clear();
        wrapped=ByteBuffer.wrap(ArrayOfHeader,10,2);
        this.lengthOfRecord=wrapped.getShort();
        wrapped.clear();
        this.flagTransaction=ArrayOfHeader[14];
        this.flagEncryption=ArrayOfHeader[15];
        System.arraycopy(ArrayOfHeader,16,this.fieldMultiUserUse,0,13);
        this.flagMDX=ArrayOfHeader[28];
        this.NumberOfDriver=ArrayOfHeader[29];

    }

    public byte[] getByteCode(){
        ByteBuffer byteBuffer=ByteBuffer.allocate(4);

        byte[] a=new byte[32];
        a[0]=this.signature;
        a[1]=this.year;
        a[2]=this.month;
        a[3]=this.day;

        byteBuffer.putInt(this.numberOfRecords);

        a[4]=byteBuffer.get(0);
        a[5]=byteBuffer.get(1);
        a[6]=byteBuffer.get(2);
        a[7]=byteBuffer.get(3);

        byteBuffer.clear();
        byteBuffer.putShort(this.lengthOfTitle);

        a[8]=byteBuffer.get(0);
        a[9]=byteBuffer.get(1);

        byteBuffer.clear();
        byteBuffer.putShort(this.lengthOfRecord);

        a[10]=byteBuffer.get(0);
        a[11]=byteBuffer.get(1);

        a[12]=0;

        a[13]=0;

        a[14]=this.flagTransaction;
        a[15]=this.flagEncryption;

        for(int i=16;i<28;i++){
            a[i]=this.fieldMultiUserUse[i-14];
        }
        a[28]=this.flagMDX;
        a[29]=this.NumberOfDriver;
        a[30]=0;
        a[31]=0;

        return a;
    }

    public void setSignature(byte signature) {
        this.signature = signature;
    }

    public void setYear(byte year) {
        this.year = year;
    }

    public void setMonth(byte month) {
        this.month = month;
    }

    public void setDay(byte day) {
        this.day = day;
    }

    public void setNumberOfRecords(int numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    public void setLengthOfTitle(short lengthOfTitle) {
        this.lengthOfTitle = lengthOfTitle;
    }

    public void setLengthOfRecord(short lengthOfRecord) {
        this.lengthOfRecord = lengthOfRecord;
    }

    public void setFlagTransaction(byte flagTransaction) {
        this.flagTransaction = flagTransaction;
    }

    public void setFlagEncryption(byte flagEncryption) {
        this.flagEncryption = flagEncryption;
    }

    public void setFieldMultiUserUse(byte[] fieldMultiUserUse) {
        this.fieldMultiUserUse = fieldMultiUserUse;
    }

    public void setFlagMDX(byte flagMDX) {
        this.flagMDX = flagMDX;
    }

    public void setNumberOfDriver(byte numberOfDriver) {
        NumberOfDriver = numberOfDriver;
    }

    public byte getSignature() {
        return signature;
    }

    public byte getYear() {
        return year;
    }

    public byte getMonth() {
        return month;
    }

    public byte getDay() {
        return day;
    }

    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    public short getLengthOfTitle() {
        return lengthOfTitle;
    }

    public short getLengthOfRecord() {
        return lengthOfRecord;
    }

    public byte getFlagTransaction() {
        return flagTransaction;
    }

    public byte getFlagEncryption() {
        return flagEncryption;
    }

    public byte[] getFieldMultiUserUse() {
        return fieldMultiUserUse;
    }

    public byte getFlagMDX() {
        return flagMDX;
    }

    public byte getNumberOfDriver() {
        return NumberOfDriver;
    }

}
