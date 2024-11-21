package com.thariq.repository;

import com.thariq.model.StudentContactNumber;
import com.thariq.model.StudentDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentContactNumberRepository extends MongoRepository<StudentContactNumber, String> {

    Optional<StudentContactNumber> findByRollNo(String rollNo);
    Optional<StudentContactNumber> findByRollNoAndIsDeletedFalse(String rollNo);  // Only active records

}
