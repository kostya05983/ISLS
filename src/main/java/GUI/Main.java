package GUI;

import SQL.Lib.Column;
import SQL.Lib.TypesOfFields;
import SQL.Parser.SelectorRequest;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.layout.AnchorPane.*;

public class Main extends Application implements Runnable {

    private TableView tableView = new TableView();
    private TextArea textIn = new TextArea();
    private Button button = new Button("ТЫК");
    private TextArea textOut = new TextArea("Здесь вывод действи библиотеки будет");

    public void start(Stage primaryStage) {

        primaryStage.setTitle("ISLS");

        //наши лэйблы
        Label name = new Label("Interpretor subset of language SQL");//наш лэйбл (потом его изменю)
        name.setId("Title-text");
        BorderPane toLabel = new BorderPane();//панелька, при помщи которой можно поставить в центр
        toLabel.setCenter(name);//двигаем панельку в центр
        BorderPane.setMargin(name, new Insets(10.0));//делаем отступы для лэйбла

        Label command = new Label ("Command:");
        setLeftAnchor(command,15.0);

        Label output = new Label("Output:");
        setLeftAnchor(output, 25.0);
        setTopAnchor(output, 20.0);

        //элемент для ввода
        setLeftAnchor(textIn, 90.0);
        setRightAnchor(textIn, 20.0);
        textIn.setMinHeight(50);

        //кнопка
        button.setMinWidth(50);
        button.setMinWidth(50);
        setLeftAnchor(button, 25.0);
        setTopAnchor(button, 30.0);

        //элемент для вывода
        setLeftAnchor(textOut, 90.0);
        setTopAnchor(textOut, 20.0);
        setRightAnchor(textOut, 20.0);
        setBottomAnchor(textOut, 20.0);
        textOut.setEditable(false);

        //таблица TableView
        setRightAnchor(tableView,20.0);
        setLeftAnchor(tableView, 20.0);
        setBottomAnchor(tableView, 20.0);

        //привязанные к краям панельки
        AnchorPane anchor1 = new AnchorPane();
        AnchorPane anchor2 = new AnchorPane();
        AnchorPane anchor3 = new AnchorPane();

        //добавление элементов в системы вёрстки
        VBox list = new VBox();//корневой список
        anchor1.getChildren().addAll(command, textIn, button);
        anchor2.getChildren().addAll(output, textOut);
        anchor3.getChildren().add(tableView);
        list.getChildren().addAll(toLabel, anchor1, anchor2, anchor3);

        //добавление элементов в окно и вызов окна
        Scene scene=new Scene(list);
        primaryStage.setScene(scene);
        primaryStage.setHeight(730);
        primaryStage.setWidth(900);
        scene.getStylesheets().add("window_style.css");
        primaryStage.show();
        initialize();
    }

    public void setAllColumns(Column[] columns){
        TableColumn[] tableColumns=new TableColumn[columns.length];
        for(int i=0;i<tableColumns.length;i++){
            tableColumns[i]=new TableColumn<List<String>,String>(columns[i].title);
            tableColumns[i].setCellValueFactory(new OurCallBack(i));
        }
        tableView.getColumns().addAll(tableColumns);

        ObservableList<List<String>> data= FXCollections.observableArrayList();
        List<String> record;
        for(int i=0;i<columns[0].data.length;i++){
            record=new ArrayList<>();
            for (Column column : columns) {
                record.add(column.data[i]);
            }
            data.add(record);
        }
        tableView.setItems(data);

    }

    //обработчик нажатий
    private void initialize()
    {
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> textGoToRelise() );

        textIn.setOnKeyPressed(event ->
        {
            if ((event.getCode().getName().equals("F1") ||
                    event.getCode().getName().equals("F5")) ||
                    (event.getCode() == KeyCode.ENTER && event.isControlDown()))
            {
                textGoToRelise();
            }
        });
    }

    //функция отправки команд на обработку
    private void textGoToRelise()
    {
        String text = textIn.getText();
        SelectorRequest selectorRequest=new SelectorRequest(text,this);
        Thread thread=new Thread(selectorRequest);
        thread.start();
    }

    //функция вывода результатов работы библиотеки
    public void outText(String text)
    {
        textOut.setText(text);
    }

    public  void run() {
        launch("");
    }

}
