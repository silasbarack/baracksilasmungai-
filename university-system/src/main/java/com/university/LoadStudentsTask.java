package com.university;
import javafx.concurrent.Task;
import java.sql.*;
import java.util.*;
public class LoadStudentsTask extends Task<List<Student>> {
    @Override protected List<Student> call() throws Exception {
        List<Student> rows=new ArrayList<>();
        try(Connection connection=DatabaseUtil.getConnection();
            PreparedStatement statement=connection.prepareStatement("SELECT student_id,registration_number,first_name,last_name FROM students ORDER BY student_id");
            ResultSet rs=statement.executeQuery()){
            while(rs.next()) {
                if(isCancelled())break;
                rows.add(new Student(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4)));
            }
        }
        return rows;
    }
}