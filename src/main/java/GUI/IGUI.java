package GUI;

import SQL.Lib.Column;
import SQL.Parser.SelectorRequest;

public interface IGUI {
    void setAllCoumns(Column[] columns);
    void setTextOutput(String textOutput);

}
