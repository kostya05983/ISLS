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
    private byte flagTransaction;//флагн на наличие незавершенной транзакции 12
    private byte flagEncryption;//флаг шифрования таблицы 13
    private byte[] fieldMultiUserUse=new byte[12];//зарезирвированная область для многопользовательского использовапния 14-28
    private byte flagMDX;//флаг наличия индексного MDX- файла 29
    private byte NumberOfDriver;//29 Идентификатор кодовой страницы файла 30


    //Конструктор Виталина
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
        this.flagTransaction=ArrayOfHeader[12];
        this.flagEncryption=ArrayOfHeader[13];
        System.arraycopy(ArrayOfHeader,14,this.fieldMultiUserUse,0,15);
        this.flagMDX=ArrayOfHeader[29];
        this.NumberOfDriver=ArrayOfHeader[30];
    }

    protected byte[] getByteCode(){
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

        a[12]=this.flagTransaction;

        a[13]=this.flagEncryption;

        for(int i=14;i<29;i++){
            a[i]=this.fieldMultiUserUse[i-14];
        }
        a[29]=this.flagMDX;
        a[30]=this.NumberOfDriver;

        return a;
    }
}
