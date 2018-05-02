package SQL.Lib.DataHandler;


import SQL.Lib.Dbf.DataDbf;
import SQL.Lib.Indexes.DataIdx;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class WriterDbf extends DataHandler {

    public WriterDbf(String nameOfFile) throws IOException {
        File file = new File(nameOfFile);
        file.delete();
        randomAccessFile = new RandomAccessFile(nameOfFile, "rw");
    }

    public WriterDbf() {
    }

    public void write(DataDbf dataDbf) throws IOException {
        int sumOfBytes = 0;
        int buf;

        randomAccessFile.write(dataDbf.headerDbf.getByteCode(), 0, 32);

        for (int i = 0; i < dataDbf.fieldsDbf.size(); i++) {
            sumOfBytes += dataDbf.fieldsDbf.get(i).getSizeField();
            randomAccessFile.write(dataDbf.fieldsDbf.get(i).getByteCode(), 0, 32);
        }

        randomAccessFile.writeByte(13);
        sumOfBytes++;

        if (dataDbf.recordsDbf != null)
            for (int i = 0; i < dataDbf.recordsDbf.size(); i++) {
                buf = sumOfBytes;
                if (dataDbf.recordsDbf.get(i).getByteCode().length == sumOfBytes)
                    randomAccessFile.write(dataDbf.recordsDbf.get(i).getByteCode(), 0, dataDbf.recordsDbf.get(i).getByteCode().length);
                else {
                    randomAccessFile.write(dataDbf.recordsDbf.get(i).getByteCode(), 0, dataDbf.recordsDbf.get(i).getByteCode().length);
                    buf -= dataDbf.recordsDbf.get(i).getByteCode().length;
                    for (int j = 0; j < buf; j++) {
                        randomAccessFile.write(0);
                    }
                }
            }

        randomAccessFile.writeByte(12);
    }

    public void write(DataIdx dataIdx) {
        try {
            randomAccessFile.write(dataIdx.getHeaderIdx().getByteCode());
            randomAccessFile.write(dataIdx.getbTreeIdx().getByteCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String name) {
        File file = new File(name + ".dbf");
        file.delete();
    }
}
