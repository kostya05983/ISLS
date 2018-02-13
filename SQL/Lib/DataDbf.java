package SQL.Lib;

public class DataDbf {


    protected HeaderDbf headerDbf;
    protected FieldDbf[] fieldsDbf;
    protected RecordDbf[] recordsDbf;

    public DataDbf(HeaderDbf headerDbf,FieldDbf[] fieldDbf,RecordDbf[] recordDbf){
        this.headerDbf=headerDbf;
        this.fieldsDbf=fieldDbf;
        this.recordsDbf=recordDbf;
    }


}
