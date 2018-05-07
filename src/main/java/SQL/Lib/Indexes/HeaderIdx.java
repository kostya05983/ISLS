package SQL.Lib.Indexes;

import java.nio.ByteBuffer;

public class HeaderIdx {

    //region Parameters

    private int rootPointer;
    private int freePointer;
    private int endPointer;
    private short keyLength;
    private byte featuresIndex;
    private byte signature;
    private byte[] keyExpression = new byte[220];
    private byte[] forExpression = new byte[220];

    //endregion

    //region Constructors

    HeaderIdx(String field) {
        rootPointer = 512;
        freePointer = 512;
        endPointer = 1023;
        keyLength = (short) field.length();
        featuresIndex = (byte) 1;
        System.arraycopy(field.getBytes(), 0, keyExpression, 0, field.getBytes().length);
    }

    public HeaderIdx() {

    }

    //endregion

    //region InterfaceMethods

    public int getEndPointer() {
        return endPointer;
    }

    void setEndPointer(int endPointer) {
        this.endPointer = endPointer;
    }

    public byte[] getByteCode() {
        ByteBuffer result = ByteBuffer.allocate(512);
        result.putInt(rootPointer);
        result.putInt(freePointer);
        result.putInt(endPointer);
        result.putShort(keyLength);
        result.put(featuresIndex);
        result.put(signature);
        result.put(keyExpression);
        result.put(forExpression);
        return result.array();
    }

    public void setByteCode(byte[] byteCode) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteCode);
        rootPointer = byteBuffer.getInt(0);
        freePointer = byteBuffer.getInt(4);
        endPointer = byteBuffer.getInt(8);
        keyLength = byteBuffer.getShort(12);
        featuresIndex = byteBuffer.get(14);
        signature = byteBuffer.get(15);
        System.arraycopy(byteBuffer.array(), 16, keyExpression, 0, 220);
        System.arraycopy(byteBuffer.array(), 236, keyExpression, 0, 220);

    }

    //endregion
}
