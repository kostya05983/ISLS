package GUI;

import SQL.Lib.AdditionalInstruments.Column;
import SQL.Parser.SelectorRequest;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.layout.AnchorPane.*;

public class Main extends Application implements Runnable {

    private TableView tableView = new TableView();
    private TextArea textIn = new TextArea();
    private Button button = new Button("ТЫК");
    private TextArea textOut = new TextArea("Здесь вывод действи библиотеки будет");
    private boolean text_start = false;

    private Duration animationDuration = new Duration(1500);
    private Duration animationDuration_undo = new Duration(500);

    private Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(tableView.maxHeightProperty(), 10)),
            new KeyFrame(animationDuration, new KeyValue(tableView.maxHeightProperty(), 350))
    );

    private Timeline timeline_undo = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(tableView.maxHeightProperty(), 350)),
            new KeyFrame(animationDuration_undo, new KeyValue(tableView.maxHeightProperty(), 10))
    );

    private AnchorPane anchor3 = new AnchorPane();

    private static final Tooltip customTooltip = new Tooltip();

    public void start(Stage primaryStage) {

        primaryStage.setTitle("ISLS");

        //наши лэйблы
        Label name = new Label("Interpretor subset of language SQL");//наш лэйбл (потом его изменю)
        name.setId("Title-text");
        BorderPane toLabel = new BorderPane();//панелька, при помщи которой можно поставить в центр
        toLabel.setCenter(name);//двигаем панельку в центр
        BorderPane.setMargin(name, new Insets(10.0));//делаем отступы для лэйбла

        Label command = new Label("Command:");
        setLeftAnchor(command, 15.0);

        Label output = new Label("Output:");
        setLeftAnchor(output, 25.0);
        setTopAnchor(output, 20.0);

        //элемент для ввода
        setLeftAnchor(textIn, 90.0);
        setRightAnchor(textIn, 20.0);
        textIn.setMinHeight(50);
        //подсказка при наведении на ввод
        textIn.setTooltip(new Tooltip("Сюда вводи команды"));

        //кнопка
        button.setMinWidth(50);
        button.setMinWidth(50);
        setLeftAnchor(button, 25.0);
        setTopAnchor(button, 30.0);
        //подсказка при наведении на кнопку
        button.setTooltip(new Tooltip("Используй Ctrl+Enter или F1 или F5"));

        //элемент для вывода
        setLeftAnchor(textOut, 90.0);
        setTopAnchor(textOut, 20.0);
        setRightAnchor(textOut, 20.0);
        setBottomAnchor(textOut, 20.0);
        textOut.setEditable(false);
        //подсказка при наведении на вывод
        textOut.setTooltip(new Tooltip("Здесь выводится результат работы библиотеки"));

        //таблица TableView
        setRightAnchor(tableView, 20.0);
        setLeftAnchor(tableView, 20.0);
        setBottomAnchor(tableView, 20.0);
        tableView.setMaxSize(10, 10);

        //привязанные к краям панельки
        AnchorPane anchor1 = new AnchorPane();
        AnchorPane anchor2 = new AnchorPane();
        //AnchorPane anchor3 = new AnchorPane();

        //добавление элементов в системы вёрстки
        VBox list = new VBox();//корневой список
        anchor1.getChildren().addAll(command, textIn, button);
        anchor2.getChildren().addAll(output, textOut);
        anchor3.getChildren().add(tableView);
        list.getChildren().addAll(toLabel, anchor1, anchor2, anchor3);

        //добавление элементов в окно и вызов окна
        Scene scene = new Scene(list);
        primaryStage.setScene(scene);
        primaryStage.setWidth(900);
        primaryStage.setHeight(730);
        scene.getStylesheets().add("window_style.css");
        initialize();
        primaryStage.show();
    }

    public void clearTable() {
        //запуск анимации свёртывания таблицы
        timeline_undo.play();
        int size = tableView.getColumns().size();
        for (int i = 0; i < size; i++)
            tableView.getColumns().remove(0);
    }

    public void setAllColumns(Column[] columns) {
        //запуск анимации
        timeline.play();

        TableColumn[] tableColumns = new TableColumn[columns.length];
        for (int i = 0; i < tableColumns.length; i++) {
            tableColumns[i] = new TableColumn<List<String>, String>(columns[i].title);
            tableColumns[i].setCellValueFactory(new OurCallBack(i));
        }
        tableView.getColumns().addAll(tableColumns);

        ObservableList<List<String>> data = FXCollections.observableArrayList();
        List<String> record;
        if (columns[0].data != null) {
            for (int i = 0; i < columns[0].data.length; i++) {
                record = new ArrayList<>();
                for (Column column : columns) {
                    record.add(column.data[i]);
                }
                data.add(record);
            }
            tableView.setItems(data);
        }
        customTooltip.hide();
    }

    //обработчик нажатий
    private void initialize() {
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> textGoToRelise());

        textIn.setOnKeyPressed(event ->
        {
            if ((event.getCode().getName().equals("F1") ||
                    event.getCode().getName().equals("F5")) ||
                    (event.getCode() == KeyCode.ENTER && event.isControlDown())) {
                textGoToRelise();
            }
        });
    }

    //функция отправки команд на обработку
    private void textGoToRelise() {
        //showTooltip(anchor3, tableView, "Мы работаем над этим...", null);
        /////////////////
        String text = textIn.getText();
        if (!text_start) { //если изначльный текст не был очищен
            textOut.clear();
            text_start = true;
        }
        SelectorRequest selectorRequest = new SelectorRequest(text, this);
        Thread thread = new Thread(selectorRequest);
        thread.start();
    }

    //функция вывода результатов работы библиотеки
    public void outText(String text) {
        textOut.setText(text + "\n" + textOut.getText());
    }

    //из вне вызывать эту функцию без параметра (для неопознанных ошибок)
    public void error() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText("Упс, что-то пошло не так! :(");
        alert.showAndWait();
    }

    //из вне вызывать эту функцию с входным параметром типа string
    public void error(String text_error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("Упс, произошла ошибка!");
        alert.setContentText(text_error);
        alert.showAndWait();
    }

    private static void showTooltip(AnchorPane owner, Control control, String tooltipText,
                                    ImageView tooltipGraphic) {
        Point2D p = control.localToScene(350.0, 50.0);
        customTooltip.setText(tooltipText);

        control.setTooltip(customTooltip);
        customTooltip.setAutoHide(true);

        customTooltip.show(owner, p.getX()
                + control.getScene().getX() + control.getScene().getWindow().getX(), p.getY()
                + control.getScene().getY() + control.getScene().getWindow().getY());
    }

    public void run() {
        launch("");
    }

    private void loading() {
        //сюда надо анимацию загрузки
        //над этим надо ещё подумать, может и текущей анимации хватит
    }
}
