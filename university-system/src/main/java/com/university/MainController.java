package com.university;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
public class MainController {
    @FXML private BorderPane mainPane;
    @FXML private Label statusLabel;
    @FXML private void showStudents(){
        try {
            Parent view=FXMLLoader.load(getClass().getResource("/com/university/StudentView.fxml"));
            mainPane.setCenter(view);
            statusLabel.setText("Student management loaded");
        } catch(Exception e) { statusLabel.setText("Cannot load view: "+e.getMessage()); }
    }
    @FXML private void initialize(){showStudents();}
    @FXML private void showDashboard(){
        mainPane.setCenter(new Label("Welcome to University Management System"));
        statusLabel.setText("Dashboard");
    }
    public void updateHeartbeat(){
        statusLabel.setText("Last heartbeat: "+java.time.LocalTime.now().withNano(0));
    }
}