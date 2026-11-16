package edu.riara.edp.week06;
import javafx.application.Application;import javafx.fxml.FXMLLoader;import javafx.scene.*;import javafx.stage.Stage;
public class FxmlDemo extends Application {public void start(Stage s)throws Exception{s.setScene(new Scene(FXMLLoader.load(getClass().getResource("/week06/Hello.fxml")),400,220));s.show();}public static void main(String[]a){launch(a);}}