package SQL.Lib;

public class RecordDbf {
    private byte headerByte;
    private byte[] data;

    public RecordDbf(byte headerByte,int sumOfRecordsByte){
        this.headerByte=headerByte;
        this.data=new byte[sumOfRecordsByte];
    }

    //Метод для пролучения всех байт
    protected byte[] getByteCode(){
        byte [] result=new byte[data.length+1];

        result[0]=headerByte;

        for(int i=1;i<data.length+1;i++){
            result[i]=data[i];
        }

        return result;
    }

}
