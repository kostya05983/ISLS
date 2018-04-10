package SQL.Lib.DataHandler;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class DataHandler {
     RandomAccessFile randomAccessFile;

     public void close(){
         try {
             randomAccessFile.close();
         } catch (IOException e) {
             out_stack_error(e.getLocalizedMessage(), e.getMessage());
         }
     }

     void out_stack_error(String textHeader, String textContent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("WriterDbf info");
        alert.setHeaderText(textHeader);
        alert.setContentText(textContent);
        alert.showAndWait();
    }
}
