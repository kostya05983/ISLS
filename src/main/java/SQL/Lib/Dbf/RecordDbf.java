package SQL.Lib.Dbf;


public class RecordDbf {

    //region Parameters

    private byte headerByte;
    private byte[] data;

    //endregion

    //region Constructors

    public RecordDbf() {

    }

    public RecordDbf(byte[] array) {
        this.data = new byte[array.length - 1];
        this.headerByte = array[0];
        System.arraycopy(array, 1, this.data, 0, array.length - 1);
    }

    //endregion

    //region InterfaceMethods

    public byte[] getByteCode() {
        byte[] result = new byte[data.length + 1];

        result[0] = headerByte;

        System.arraycopy(data, 0, result, 1, data.length + 1 - 1);

        return result;
    }

    void setByteCode(byte header, byte[] array) {
        this.headerByte = header;
        this.data = new byte[array.length];

        System.arraycopy(array, 0, this.data, 0, array.length);
    }

    byte getHeaderByte() {
        return headerByte;
    }

    public void setHeaderByte(byte headerByte) {
        this.headerByte = headerByte;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    String getPartOfRecord(int start, int size) {
        byte[] buf = new byte[size];
        System.arraycopy(data, start, buf, 0, size);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (buf[i] == 0)
                break;
        }
        byte[] result = new byte[i];
        System.arraycopy(buf, 0, result, 0, i);

        return new String(result);
    }

    //endregion
}
