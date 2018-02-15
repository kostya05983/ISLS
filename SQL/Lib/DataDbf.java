package SQL.Lib;

public class DataDbf {


    public HeaderDbf headerDbf;
     public FieldDbf[] fieldsDbf;
    public RecordDbf[] recordsDbf;

    public DataDbf(HeaderDbf headerDbf,FieldDbf[] fieldDbf,RecordDbf[] recordDbf){
        this.headerDbf=headerDbf;
        this.fieldsDbf=fieldDbf;
        this.recordsDbf=recordDbf;
    }


}
