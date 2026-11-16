package com.university;
import javafx.fxml.FXML;
import javafx.collections.*;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import java.sql.*;
import java.util.concurrent.atomic.AtomicBoolean;
public class StudentController {
    @FXML private TextField regField,firstNameField,lastNameField;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student,String> colReg,colFirst,colLast;
    @FXML private Label statusLabel;
    @FXML private Button saveButton;
    private final ObservableList<Student> students=FXCollections.observableArrayList();
    private final AtomicBoolean loading=new AtomicBoolean();
    @FXML private void initialize(){
        colReg.setCellValueFactory(c->c.getValue().regNumberProperty());
        colFirst.setCellValueFactory(c->c.getValue().firstNameProperty());
        colLast.setCellValueFactory(c->c.getValue().lastNameProperty());
        studentTable.setItems(students);
        saveButton.disableProperty().bind(
            regField.textProperty().isEmpty().or(firstNameField.textProperty().isEmpty()).or(lastNameField.textProperty().isEmpty()));
        loadStudents();
    }
    @FXML private void handleSaveStudent(){
        String reg=regField.getText().trim(),first=firstNameField.getText().trim(),last=lastNameField.getText().trim();
        if(reg.isEmpty()||first.isEmpty()||last.isEmpty()||reg.length()>20||first.length()>50||last.length()>50){
            statusLabel.setText("Check required fields and length limits");return;
        }
        Task<Void> task=new Task<>(){protected Void call()throws Exception{
            try(Connection conn=DatabaseUtil.getConnection();PreparedStatement stmt=conn.prepareStatement(
                "INSERT INTO students (registration_number,first_name,last_name,email) VALUES (?,?,?,?)")){
                stmt.setString(1,reg);stmt.setString(2,first);stmt.setString(3,last);
                stmt.setString(4,reg+"@example.edu");stmt.executeUpdate();
            }
            return null;
        }};
        statusLabel.setText("Saving student...");
        task.setOnSucceeded(e->{regField.clear();firstNameField.clear();lastNameField.clear();statusLabel.setText("Student saved");loadStudents();});
        task.setOnFailed(e->statusLabel.setText("Save failed: "+task.getException().getMessage()));
        start(task,"student-save");
    }
    @FXML private void handleRefresh(){loadStudents();}
    public void loadStudents(){
        if(!loading.compareAndSet(false,true))return;
        LoadStudentsTask task=new LoadStudentsTask();
        task.setOnSucceeded(e->{loading.set(false);students.setAll(task.getValue());statusLabel.setText("Loaded "+students.size()+" students");});
        task.setOnFailed(e->{loading.set(false);statusLabel.setText("Load failed: "+task.getException().getMessage());});
        task.setOnCancelled(e->loading.set(false));
        start(task,"student-load");
    }
    private static void start(Task<?> task,String name){
        Thread thread=new Thread(task,name);thread.setDaemon(true);thread.start();
    }
}