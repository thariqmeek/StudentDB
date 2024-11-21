package com.thariq.repository;
import com.thariq.model.StudentAssociatedSubject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAssociatedSubjectRepository extends MongoRepository<StudentAssociatedSubject, String> {
    List<StudentAssociatedSubject> findByRollNo(String rollNo);
    Optional<StudentAssociatedSubject> findByRollNoAndIsDeletedFalse(String rollNo);  // Only active records

}