package SQL.Lib.Dbf;


public class RecordDbf {
    private byte headerByte;
    private byte[] data;

    public RecordDbf(){

    }

    public RecordDbf(byte headerByte,int sumOfRecordsByte){
        this.headerByte=headerByte;
        this.data=new byte[sumOfRecordsByte];
    }

    public RecordDbf(byte headerByte,byte[] data){
        this.headerByte=headerByte;
        this.data=data;
    }

    public RecordDbf(byte[] array){
        this.data=new byte[array.length-1];
        this.headerByte=array[0];
        System.arraycopy(array, 1, this.data, 0, array.length - 1);
    }

    public byte[] getByteCode(){
        byte [] result=new byte[data.length+1];

        result[0]=headerByte;

        System.arraycopy(data, 0, result, 1, data.length + 1 - 1);

        return result;
    }

    public void setByteCode(byte[] array){
        this.headerByte=array[0];
        System.arraycopy(array, 1, this.data, 0, array.length - 1);
    }

    public void setByteCode(byte header,byte[] array){
        this.headerByte=header;
        this.data=new byte[array.length];

        System.arraycopy(array, 0, this.data, 0, array.length);
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

    String getPartOfRecord(int start, int size){
        byte[] buf=new byte[size];
        System.arraycopy(data,start,buf,0,size);
        int i;
        for(i = 0;i<buf.length;i++){
            if(buf[i]==0)
                break;
        }
        byte[] result=new byte[i];
        System.arraycopy(buf,0,result,0,i);

        return new String(result);
    }
}
