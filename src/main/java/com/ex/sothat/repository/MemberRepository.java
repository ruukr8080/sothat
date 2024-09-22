package com.ex.sothat.repository;

import com.ex.sothat.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Account, Long> {
    Optional<Account> findUserByEmailAndProvider(String email, String provider);
    Optional<Account> findUserByEmail(String email);
    Optional<Account> findByUserid(String userid);
    boolean existsByUserid(String userid);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
