package com.spring.E_Learning.Repository;

import com.spring.E_Learning.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByStudentId(int studentId);

}
