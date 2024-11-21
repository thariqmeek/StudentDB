package com.thariq.service;

import com.thariq.model.StudentAssociatedSubject;
import com.thariq.model.StudentContactNumber;
import com.thariq.model.StudentDetails;
import com.thariq.repository.StudentAssociatedSubjectRepository;
import com.thariq.repository.StudentContactNumberRepository;
import com.thariq.repository.StudentDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Autowired
    private StudentDetailsRepository studentDetailsRepository;

    @Autowired
    private StudentContactNumberRepository studentContactNumberRepository;

    @Autowired
    private StudentAssociatedSubjectRepository studentAssociatedSubjectRepository;

    public void addStudentDetails(StudentDetails student) {
        if (student.getRollNo().length() != 6) {
            throw new IllegalArgumentException("Roll number must be 6 digits.");
        }
        Optional<StudentDetails> existingStudent = studentDetailsRepository.findByRollNo(student.getRollNo());
        if (existingStudent.isPresent()) {
            throw new IllegalArgumentException("Student with this roll number already exists, even if deleted.");
        }

        // Save the new student
        student.setDeleted(false); // Mark the student as active
        studentDetailsRepository.save(student);
        System.out.println("Student details saved successfully.");



        String uniqueNo = (student.getGender().equalsIgnoreCase("male") ? "11" : "01") + student.getRollNo();

        StudentContactNumber contactNumber = new StudentContactNumber();
        contactNumber.setDeleted(false);
        contactNumber.setRollNo(student.getRollNo());
        contactNumber.setUniqueNo(uniqueNo);

        System.out.println("Saving contact number: " + contactNumber);
        studentContactNumberRepository.save(contactNumber);
        System.out.println("Contact number saved successfully.");
    }

    public void addSubject(String rollNo, String subjectName) {
        Optional<StudentDetails> studentOpt = studentDetailsRepository.findByRollNo(rollNo);
        if (studentOpt.isEmpty() || studentOpt.get().isDeleted()) {
            throw new IllegalArgumentException("Student not found.");
        }

        StudentContactNumber contactNumber = studentContactNumberRepository.findByRollNo(rollNo)
                .orElseThrow(() -> new IllegalArgumentException("Contact information not found."));

        StudentAssociatedSubject subjectRecord = studentAssociatedSubjectRepository
                .findById(rollNo)
                .orElseGet(() -> {
                    StudentAssociatedSubject newRecord = new StudentAssociatedSubject();
                    newRecord.setRollNo(rollNo);
                    newRecord.setName(studentOpt.get().getName());
                    newRecord.setUniqueNo(contactNumber.getUniqueNo());
                    newRecord.setSubjects(new HashMap<>());
                    newRecord.setDeleted(false);
                    return newRecord;
                });

        Map<String, String> subjects = subjectRecord.getSubjects();
        subjects.put("Sub_nam_" + (subjects.size() + 1), subjectName);
        studentAssociatedSubjectRepository.save(subjectRecord);
    }

    // Fetch subjects for a student by rollNo
    public List<String> getSubjectsByRollNo(String rollNo) {
        // Fetch all records of StudentAssociatedSubject for the given rollNo
        Optional<StudentAssociatedSubject> subjectRecords = studentAssociatedSubjectRepository.findByRollNoAndIsDeletedFalse(rollNo);

        if (subjectRecords.isEmpty() ) {
            throw new IllegalArgumentException("No subjects found for roll number: " + rollNo);
        }

        // Collect all subject names from all associated records
        List<String> allSubjects = subjectRecords.stream()
                .flatMap(record -> record.getSubjects().values().stream()) // Extract subjects from the map in each record
                .collect(Collectors.toList());

        return allSubjects;
    }

    public void softDeleteStudentDetails(String rollNo) {
        // Check if student exists
        Optional<StudentDetails> studentOpt = studentDetailsRepository.findByRollNoAndIsDeletedFalse(rollNo);
        if (studentOpt.isEmpty()) {
            throw new IllegalArgumentException("Student with roll number " + rollNo + " not found.");
        }

        // Soft delete the student record by setting isDeleted to true
        StudentDetails studentDetails = studentOpt.get();
        studentDetails.setDeleted(true);
        studentDetailsRepository.save(studentDetails);

        // Soft delete the associated contact number record
        Optional<StudentContactNumber> contactNumberOpt = studentContactNumberRepository.findByRollNoAndIsDeletedFalse(rollNo);
        contactNumberOpt.ifPresent(contactNumber -> {
            contactNumber.setDeleted(true);
            studentContactNumberRepository.save(contactNumber);
        });

        // Soft delete the associated subject record
        Optional<StudentAssociatedSubject> subjectOpt = studentAssociatedSubjectRepository.findByRollNoAndIsDeletedFalse(rollNo);
        subjectOpt.ifPresent(subject -> {
            subject.setDeleted(true);
            studentAssociatedSubjectRepository.save(subject);
        });

        System.out.println("Student and associated data marked as deleted for rollNo: " + rollNo);
    }

    // You can create a method to reactivate the student if needed
    public void reactivateStudentDetails(String rollNo) {
        Optional<StudentDetails> studentOpt = studentDetailsRepository.findByRollNoAndIsDeletedTrue(rollNo);
        if (studentOpt.isEmpty()) {
            throw new IllegalArgumentException("Student with roll number " + rollNo + " not found.");
        }

        // Reactivate the student by setting isDeleted to false
        StudentDetails studentDetails = studentOpt.get();
        studentDetails.setDeleted(false);
        studentDetailsRepository.save(studentDetails);

        // Reactivate the associated contact number record
        Optional<StudentContactNumber> contactNumberOpt = studentContactNumberRepository.findByRollNoAndIsDeletedFalse(rollNo);
        contactNumberOpt.ifPresent(contactNumber -> {
            contactNumber.setDeleted(false);
            studentContactNumberRepository.save(contactNumber);
        });

        // Reactivate the associated subject record
        Optional<StudentAssociatedSubject> subjectOpt = studentAssociatedSubjectRepository.findByRollNoAndIsDeletedFalse(rollNo);
        subjectOpt.ifPresent(subject -> {
            subject.setDeleted(false);
            studentAssociatedSubjectRepository.save(subject);
        });

        System.out.println("Student and associated data reactivated for rollNo: " + rollNo);
    }
}
