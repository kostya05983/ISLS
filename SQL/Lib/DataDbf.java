package SQL.Lib;

public class DataDbf {


    private HeaderDbf headerDbf;
    private FieldDbf[] fieldsDbf;
    private RecordDbf[] recordsDbf;

    public DataDbf(HeaderDbf headerDbf,FieldDbf[] fieldDbf,RecordDbf[] recordDbf){
        this.headerDbf=headerDbf;
        this.fieldsDbf=fieldDbf;
        this.recordsDbf=recordDbf;
    }

    public HeaderDbf getHeaderDbf() {
        return headerDbf;
    }

    public FieldDbf[] getFieldsDbf() {
        return fieldsDbf;
    }

    public RecordDbf[] getRecordsDbf() {
        return recordsDbf;
    }

}
