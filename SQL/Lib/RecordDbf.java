package SQL.Lib;

public class RecordDbf {
    private byte headerByte;
    private byte[] data;

    public RecordDbf(byte headerByte,int sumOfRecordsByte){
        this.headerByte=headerByte;
        this.data=new byte[sumOfRecordsByte];
    }

}
