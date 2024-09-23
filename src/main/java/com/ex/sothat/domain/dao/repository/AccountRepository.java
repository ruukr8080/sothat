package com.ex.sothat.domain.dao.repository;

import com.ex.sothat.domain.dao.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findUserByEmailAndProvider(String email, String provider);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByName(String name);

    boolean existsByEmail(String email);

    boolean existsByName(String nickname);
}
