package SQL.Lib.Indexes;



public class BTreeIdx {
    private static final int M=4;
    private Node root;
    private int height;
    private int amountPairs;

    private final static class Node{

    }

    private static class Entry{
        private short keyAmount;
        private byte[] data=new byte[500];
    }
}
