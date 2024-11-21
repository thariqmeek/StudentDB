package com.thariq.controller;

import com.thariq.model.StudentDetails;
import com.thariq.model.StudentAssociatedSubject;
import com.thariq.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@ComponentScan
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    // Add a new student
    @PostMapping("/add")
    public ResponseEntity<String> addStudent(@RequestBody StudentDetails student) {
        try {
            studentService.addStudentDetails(student);
            return ResponseEntity.ok("Student details added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Add a subject to a student
    @PostMapping("/{rollNo}/subjects")
    public ResponseEntity<String> addSubject(@PathVariable String rollNo, @RequestParam String subjectName) {
        try {
            studentService.addSubject(rollNo, subjectName);
            return ResponseEntity.ok("Subject added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Fetch subjects of a student using rollNo
    @GetMapping("/{rollNo}/subjectslist")
    public ResponseEntity<List<String>> getSubjects(@PathVariable String rollNo) {
        try {
            List<String> subjects = studentService.getSubjectsByRollNo(rollNo);
            return ResponseEntity.ok(subjects);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);  // Handle appropriately
        }
    }

    // Soft Delete a student by rollNo
    @DeleteMapping("/{rollNo}/deletestudent")
    public ResponseEntity<String> softDeleteStudent(@PathVariable String rollNo) {
        try {
            studentService.softDeleteStudentDetails(rollNo);
            return ResponseEntity.ok("Student and associated data marked as deleted.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Reactivate a student by rollNo
    @PutMapping("/{rollNo}/reactivate")
    public ResponseEntity<String> reactivateStudent(@PathVariable String rollNo) {
        try {
            studentService.reactivateStudentDetails(rollNo);
            return ResponseEntity.ok("Student and associated data reactivated.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
