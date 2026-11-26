package com.university;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalTime;

public class MainController {
    @FXML private BorderPane mainPane;
    @FXML private Label statusLabel;
    private final Label studentCount = new Label("Students: loading...");
    private final Label programCount = new Label("Programs: loading...");
    private final Label refreshedAt = new Label("Waiting for database");
    private StudentController activeStudents;
    private boolean dashboardVisible;
    private boolean refreshing;

    @FXML private void initialize() { showDashboard(); }

    @FXML private void showStudents() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/university/StudentView.fxml"));
            Parent view = loader.load();
            activeStudents = loader.getController();
            dashboardVisible = false;
            mainPane.setCenter(view);
            statusLabel.setText("Student management loaded");
        } catch (Exception e) {
            statusLabel.setText("Cannot load view: " + e.getMessage());
        }
    }

    @FXML private void showDashboard() {
        activeStudents = null;
        dashboardVisible = true;
        VBox dashboard = new VBox(15, new Label("University Management Dashboard"),
            studentCount, programCount, refreshedAt);
        dashboard.setStyle("-fx-padding: 24;");
        mainPane.setCenter(dashboard);
        refreshDashboard();
    }

    // Called on the JavaFX Application Thread by the five-second scheduler.
    // Only start database work here; the query itself runs on a worker thread.
    public void refreshDashboard() {
        if (activeStudents != null) activeStudents.loadStudents();
        if (!dashboardVisible || refreshing) return;
        refreshing = true;
        Task<long[]> task = new Task<>() {
            @Override protected long[] call() throws Exception {
                try (Connection connection = DatabaseUtil.getConnection();
                     PreparedStatement statement = connection.prepareStatement(
                         "SELECT (SELECT COUNT(*) FROM students), (SELECT COUNT(*) FROM programs)")) {
                    statement.setQueryTimeout(4);
                    try (ResultSet rs = statement.executeQuery()) {
                        if (!rs.next()) throw new IllegalStateException("No dashboard counts returned");
                        return new long[]{rs.getLong(1), rs.getLong(2)};
                    }
                }
            }
        };
        task.setOnSucceeded(event -> {
            refreshing = false;
            long[] counts = task.getValue();
            studentCount.setText("Students: " + counts[0]);
            programCount.setText("Programs: " + counts[1]);
            refreshedAt.setText("Last refreshed: " + LocalTime.now().withNano(0));
            if (dashboardVisible) statusLabel.setText("Dashboard refreshed");
        });
        task.setOnFailed(event -> {
            refreshing = false;
            refreshedAt.setText("Refresh failed: " + task.getException().getMessage());
            if (dashboardVisible) statusLabel.setText("Database unavailable; retrying automatically");
        });
        task.setOnCancelled(event -> refreshing = false);
        Thread worker = new Thread(task, "dashboard-refresh");
        worker.setDaemon(true);
        worker.start();
    }
}
