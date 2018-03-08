package SQL.Lib;

public class RecordDbf {
    private byte headerByte;
    private byte[] data;

    public RecordDbf(){

    }

    public RecordDbf(byte headerByte,int sumOfRecordsByte){
        this.headerByte=headerByte;
        this.data=new byte[sumOfRecordsByte];
    }
    //Метод для получения всех байт
    public byte[] getByteCode(){
        byte [] result=new byte[data.length+1];

        result[0]=headerByte;

        for(int i=1;i<data.length+1;i++){
            result[i]=data[i-1];
        }

        return result;
    }

    public void setByteCode(byte[] array){
        this.headerByte=array[0];
        for(int i=1;i<array.length;i++)
            this.data[i-1]=array[i];
    }

    public void setByteCode(byte header,byte[] array){
        this.headerByte=header;
        this.data=new byte[array.length];

        for(int i=0;i<array.length;i++)
            this.data[i]=array[i];
    }

    public void setHeaderByte(byte headerByte) {
        this.headerByte = headerByte;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte getHeaderByte() {
        return headerByte;
    }

    public byte[] getData() {
        return data;
    }
}
