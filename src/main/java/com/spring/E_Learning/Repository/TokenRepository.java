package com.spring.E_Learning.Repository;

import com.spring.E_Learning.Model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Token findByToken(String token);
}
