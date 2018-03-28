package SQL.Lib;

public class DataIdx {
    private int rootPointer;
    private int freePointer;
    private int endPointer;
    private short keyLength;
    private byte featuressIndex;
    private byte signature;
    private byte[] keyExpression=new byte[220];
    private byte[] forExpression=new byte[220];

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

    public byte getFeaturessIndex() {
        return featuressIndex;
    }

    public void setFeaturessIndex(byte featuressIndex) {
        this.featuressIndex = featuressIndex;
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
