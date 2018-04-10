package SQL.Lib.Indexes;

import SQL.Lib.Dbf.DataDbf;

import java.util.Random;

public class DataIdx {

    public DataIdx(String field, DataDbf dataDbf,int amount){
       String key=generateKey(field,amount);
       headerIdx=new HeaderIdx(field);
       bTreeIdx=new BTreeIdx(field.split(""),dataDbf.getPositions(field));
       bTreeIdx.init();
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

    private HeaderIdx headerIdx;
    private BTreeIdx bTreeIdx;
}
