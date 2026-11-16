package com.university;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import java.util.concurrent.*;
public class App extends Application {
    private ScheduledExecutorService scheduler;
    @Override public void start(Stage stage) throws Exception {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/university/MainView.fxml"));
        Parent root=loader.load();MainController controller=loader.getController();
        stage.setTitle("University Management System");stage.setScene(new Scene(root,850,570));stage.show();
        scheduler=Executors.newSingleThreadScheduledExecutor(r->{Thread t=new Thread(r,"university-heartbeat");t.setDaemon(true);return t;});
        scheduler.scheduleAtFixedRate(()->Platform.runLater(controller::updateHeartbeat),0,5,TimeUnit.SECONDS);
    }
    @Override public void stop(){if(scheduler!=null)scheduler.shutdownNow();}
    public static void main(String[]args){launch(args);}
}