package SQL.Lib.Indexes;

import java.nio.ByteBuffer;

public class HeaderIdx {
    private int rootPointer;
    private int freePointer;
    private int endPointer;
    private short keyLength;
    private byte featuresIndex;
    private byte signature;
    private byte[] keyExpression=new byte[220];
    private byte[] forExpression=new byte[220];

    HeaderIdx(String field){
        rootPointer=512;
        freePointer=512;
        endPointer=1023;
        keyLength=(short)field.length();
        featuresIndex=(byte)1;
        System.arraycopy(field.getBytes(),0,keyExpression,0,field.getBytes().length);
    }

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
}