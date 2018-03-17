package GUI;


import SQL.Lib.Column;
import SQL.Lib.TypesOfFields;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Main extends Application {
    private TableView table=new TableView();

    public void start(Stage primaryStage) {

        primaryStage.setTitle("APP");

        FlowPane flowPane=new FlowPane(Orientation.VERTICAL);

        TextField textIn = new TextField();
        TextField textOut = new TextField();

        //Stage toast = new Stage();
        Column[] buf=new Column[2];

        // Column buf1=new Column();
        buf[0]=new Column();
        buf[0].size=5;
        buf[0].data=new String[2];
        buf[0].title=new String("lol");
        buf[0].type= TypesOfFields.Character;
        buf[1]=new Column();
        buf[1].size=5;
        buf[1].type=TypesOfFields.Character;
        buf[1].title=new String("lolo");
        buf[1].data=new String[]{"11111","22222"};

        setAllColumns(buf);


        Button button=new Button("ОЛОЛ");
        flowPane.setPadding(new Insets(100,100,100,100));
        flowPane.getChildren().addAll(textIn, textOut, table, button);



        Scene scene=new Scene(flowPane);
        primaryStage.setScene(scene);
        primaryStage.setHeight(800);
        primaryStage.setWidth(800);

        primaryStage.show();
    }
    public void setAllColumns(Column[] columns){
        TableColumn[] tableColumns=new TableColumn[columns.length];
        for(int i=0;i<tableColumns.length;i++){
            tableColumns[i]=new TableColumn(columns[i].title);
        }
        table.getColumns().addAll(tableColumns);
        ObservableList<String> data= FXCollections.observableArrayList();
        table.setItems(data);

        for(int i=0;i<columns.length;i++){//Он почему-то не хочет их отображать) Хотя в лист они добавляются
           for(int j=0;j<columns[i].data.length;j++)
            data.add(j,columns[i].data[j]);
        }


    }
    public static void main(String[] args) {
        launch(args);
    }
}
