package SQL.Lib.Indexes;

import SQL.Lib.Dbf.DataDbf;

import java.util.Random;

public class DataIdx {

    private HeaderIdx headerIdx;
    private BTreeIdx bTreeIdx;


    public DataIdx(String field, DataDbf dataDbf,int amount){
       String key=generateKey(field,amount);
       headerIdx=new HeaderIdx(field);
       headerIdx.setEndPointer(field.length()*512);
       bTreeIdx=new BTreeIdx(field.split(""),dataDbf.getPositions(field));
       bTreeIdx.init();
    }

    public DataIdx(HeaderIdx headerIdx,BTreeIdx bTreeIdx){
        this.headerIdx=headerIdx;
        this.bTreeIdx=bTreeIdx;
    }

    private String generateKey(String field,int amount){
        if((field.length()-2)>(amount/62)&&field.length()>=3) {
            return field;
        }else{
            Random random=new Random(System.currentTimeMillis());
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(field);
            for(int i=0;i<amount/62-field.length();i++){
                stringBuilder.append((char)(random.nextDouble()*25+65));
            }
            return stringBuilder.toString();
        }
    }

    public HeaderIdx getHeaderIdx() {
        return headerIdx;
    }

    public void setHeaderIdx(HeaderIdx headerIdx) {
        this.headerIdx = headerIdx;
    }

    public BTreeIdx getbTreeIdx() {
        return bTreeIdx;
    }

    public void setbTreeIdx(BTreeIdx bTreeIdx) {
        this.bTreeIdx = bTreeIdx;
    }
}
