package GUI;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.List;

public class OurCallBack implements Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>> {
    private int i;

    OurCallBack(int i) {
        this.i = i;
    }

    @Override
    public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> param) {
        return new ReadOnlyStringWrapper(param.getValue().get(i));
    }
}
