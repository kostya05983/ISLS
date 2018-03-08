//package GUI;
//
//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.geometry.Orientation;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.TableView;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.FlowPane;
//import javafx.stage.Stage;
//
//
//public class Main extends Application {
//
//
//    public void start(Stage primaryStage) throws Exception{
//        primaryStage.setTitle("APP");
//
//        FlowPane flowPane=new FlowPane(Orientation.VERTICAL);
//
//        TextField textIn = new TextField();
//        TextField textOut = new TextField();
//
//        //Stage toast = new Stage();
//
//        TableView table = new TableView();
//        Button button=new Button("ОЛОЛ");
//        flowPane.setPadding(new Insets(100,100,100,100));
//        flowPane.getChildren().addAll(textIn, textOut, table, button);
//
//
//
//        Scene scene=new Scene(flowPane);
//        primaryStage.setScene(scene);
//        primaryStage.setHeight(800);
//        primaryStage.setWidth(800);
//
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
