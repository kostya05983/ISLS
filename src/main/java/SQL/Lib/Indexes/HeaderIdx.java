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
    private byte[] keyExpression=new byte[220];
    private byte[] forExpression=new byte[220];

    //endregion

    //region Constructors

    HeaderIdx(String field){
        rootPointer=512;
        freePointer=512;
        endPointer=1023;
        keyLength=(short)field.length();
        featuresIndex=(byte)1;
        System.arraycopy(field.getBytes(),0,keyExpression,0,field.getBytes().length);
    }

    public HeaderIdx(){

    }

    //endregion

    //region InterfaceMethods

    public int getRootPointer() {
        return rootPointer;
    }

    public void setRootPointer(int rootPointer) {
        this.rootPointer = rootPointer;
    }

    public int getFreePointer() {
        return freePointer;
    }

    public void setFreePointer(int freePointer) {
        this.freePointer = freePointer;
    }

    public int getEndPointer() {
        return endPointer;
    }

    public void setEndPointer(int endPointer) {
        this.endPointer = endPointer;
    }

    public short getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(short keyLength) {
        this.keyLength = keyLength;
    }

    public byte getFeaturesIndex() {
        return featuresIndex;
    }

    public void setFeaturesIndex(byte featuresIndex) {
        this.featuresIndex = featuresIndex;
    }

    public byte getSignature() {
        return signature;
    }

    public void setSignature(byte signature) {
        this.signature = signature;
    }

    public byte[] getKeyExpression() {
        return keyExpression;
    }

    public void setKeyExpression(byte[] keyExpression) {
        this.keyExpression = keyExpression;
    }

    public byte[] getForExpression() {
        return forExpression;
    }

    public void setForExpression(byte[] forExpression) {
        this.forExpression = forExpression;
    }

    public byte[] getByteCode(){
        ByteBuffer result=ByteBuffer.allocate(512);
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

    public void setByteCode(byte[] byteCode){
        ByteBuffer byteBuffer=ByteBuffer.wrap(byteCode);
        rootPointer=byteBuffer.getInt(0);
        freePointer=byteBuffer.getInt(4);
        endPointer=byteBuffer.getInt(8);
        keyLength=byteBuffer.getShort(12);
        featuresIndex=byteBuffer.get(14);
        signature=byteBuffer.get(15);
        System.arraycopy(byteBuffer.array(),16,keyExpression,0,220);
        System.arraycopy(byteBuffer.array(),236,keyExpression,0,220);

    }

    //endregion
}
