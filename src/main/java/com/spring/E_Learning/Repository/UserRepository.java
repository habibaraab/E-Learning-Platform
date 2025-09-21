package com.spring.E_Learning.Repository;

import com.spring.E_Learning.Enum.Role;
import com.spring.E_Learning.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByEmail(String email);
    Long countByRole(Role role);
}
