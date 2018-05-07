package SQL.Lib.DataHandler;

import SQL.Lib.Dbf.DataDbf;
import SQL.Lib.Dbf.FieldDbf;
import SQL.Lib.Dbf.HeaderDbf;
import SQL.Lib.Dbf.RecordDbf;
import SQL.Lib.Indexes.BTreeIdx;
import SQL.Lib.Indexes.DataIdx;
import SQL.Lib.Indexes.HeaderIdx;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ReaderDbf extends DataHandler {

    public ReaderDbf(String nameOfFile) throws IOException {
        randomAccessFile = new RandomAccessFile(nameOfFile, "r");
    }

    public DataDbf read() throws IOException {
        byte[] buf = new byte[32];
        long position = 32;

        randomAccessFile.read(buf);

        HeaderDbf headerDbf = new HeaderDbf(buf);

        ArrayList<FieldDbf> fieldsDbf = new ArrayList<>();
        FieldDbf fieldDbf;
        int sizeOfRecord = 0;

        while (randomAccessFile.readByte() != 13) {
            randomAccessFile.seek(position);
            randomAccessFile.read(buf);
            fieldDbf = new FieldDbf(buf);
            fieldsDbf.add(fieldDbf);
            sizeOfRecord += transferByteToUnsigned(buf[16]);
            position += 32;
        }

        position++;
        sizeOfRecord++;

        byte[] bufRecord = new byte[sizeOfRecord];
        RecordDbf recordDbf;
        ArrayList<RecordDbf> recordsDbf = new ArrayList<>();

        while (randomAccessFile.readByte() != 12) {
            randomAccessFile.seek(position);
            randomAccessFile.read(bufRecord);
            recordDbf = new RecordDbf(bufRecord);
            recordsDbf.add(recordDbf);
            position += sizeOfRecord;
        }

        return new DataDbf(headerDbf, fieldsDbf, recordsDbf);
    }

    public DataIdx readIdx() {
        HeaderIdx headerIdx = new HeaderIdx();
        BTreeIdx bTreeIdx = new BTreeIdx();
        byte[] buffer = new byte[512];

        try {
            randomAccessFile.read(buffer);
            headerIdx.setByteCode(buffer);
            buffer = new byte[headerIdx.getEndPointer() - 512];

            randomAccessFile.read(buffer);
            bTreeIdx.setByteCode(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new DataIdx(headerIdx, bTreeIdx);
    }


}
