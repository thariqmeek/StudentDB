package com.thariq.repository;

import com.thariq.model.StudentDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentDetailsRepository extends MongoRepository<StudentDetails, String> {
    Optional<StudentDetails> findByRollNo(String rollNo);
    boolean existsByRollNo(String rollNo);

    Optional<StudentDetails> findByRollNoAndIsDeletedFalse(String rollNo);

    Optional<StudentDetails> findByRollNoAndIsDeletedTrue(String rollNo);// Only active (non-deleted) records
    void deleteByRollNo(String rollNo);  // You can still keep this for actual deletion if needed
}
