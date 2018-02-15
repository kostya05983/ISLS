package SQL.Lib;

public class RecordDbf {
    private byte headerByte;
    private byte[] data;


    public RecordDbf(byte headerByte,int sumOfRecordsByte){
        this.headerByte=headerByte;
        this.data=new byte[sumOfRecordsByte];
    }
    //Метод для пролучения всех байт
    public byte[] getByteCode(){
        byte [] result=new byte[data.length+1];

        result[0]=headerByte;

        for(int i=1;i<data.length+1;i++){
            result[i]=data[i];
        }

        return result;
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
