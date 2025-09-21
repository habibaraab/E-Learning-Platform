package com.spring.E_Learning.Repository;

import com.spring.E_Learning.Enum.RequestStatus;
import com.spring.E_Learning.Enum.RequestType;
import com.spring.E_Learning.Model.StudentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRequestRepository extends JpaRepository<StudentRequest, Integer> {


    List<StudentRequest> findByStatus(RequestStatus status);
    List<StudentRequest> findByStudentId(int studentId);

    Optional<StudentRequest> findTopByStudentIdAndExamIdAndTypeAndStatus(
            Integer studentId,
            Integer examId,
            RequestType type,
            RequestStatus status
    );

    Long countByStatus(RequestStatus status);
}

