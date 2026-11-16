package com.university;
import javafx.beans.property.*;
public class Student {
    private final IntegerProperty id=new SimpleIntegerProperty();
    private final StringProperty regNumber=new SimpleStringProperty();
    private final StringProperty firstName=new SimpleStringProperty();
    private final StringProperty lastName=new SimpleStringProperty();
    public Student(int id,String reg,String first,String last){
        this.id.set(id);regNumber.set(reg);firstName.set(first);lastName.set(last);
    }
    public IntegerProperty idProperty(){return id;}
    public StringProperty regNumberProperty(){return regNumber;}
    public StringProperty firstNameProperty(){return firstName;}
    public StringProperty lastNameProperty(){return lastName;}
    public String getRegNumber(){return regNumber.get();}
}