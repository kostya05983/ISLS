package SQL.Lib.Indexes;

public class IndexVertex {
    private byte attribute;
    private short keyAmount;
    private int leftPointer;
    private int rightPointer;
    private byte[] data=new byte[500];
}
