package com.thariq.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Map;

@Document(collection = "Std_Assoc_Sub")
public class StudentAssociatedSubject {

    @Id
    private String id; // MongoDB's unique identifier

    private String rollNo; // Separate field for roll number
    private String name;
    private String uniqueNo;
    private Map<String, String> subjects;

    private boolean isDeleted;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueNo() {
        return uniqueNo;
    }

    public void setUniqueNo(String uniqueNo) {
        this.uniqueNo = uniqueNo;
    }

    public Map<String, String> getSubjects() {
        return subjects;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setSubjects(Map<String, String> subjects) {
        this.subjects = subjects;
    }




}
