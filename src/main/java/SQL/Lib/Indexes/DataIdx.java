package SQL.Lib.Indexes;

import SQL.Lib.Dbf.DataDbf;

import java.util.Random;

public class DataIdx {

    //region Parameters

    private HeaderIdx headerIdx;
    private BTreeIdx bTreeIdx;

    //endregion

    //region Constructors

    public DataIdx(String fieldName, DataDbf dataDbf){
       String key=generateKey(fieldName,dataDbf.recordsDbf.size());
       headerIdx=new HeaderIdx(fieldName);
       headerIdx.setEndPointer(fieldName.length()*512+512);
       bTreeIdx=new BTreeIdx(fieldName.split(""),dataDbf.getPositions(fieldName));
       bTreeIdx.init();
    }

    public DataIdx(HeaderIdx headerIdx,BTreeIdx bTreeIdx){
        this.headerIdx=headerIdx;
        this.bTreeIdx=bTreeIdx;
    }

    //endregion

    //region InterfaceMethods

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

    //endregion

    //region PrivateMethods

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

    //endregion


}
