package com.ex.sothat.domain.dao.repository;

import com.ex.sothat.domain.dao.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByNickname(String Nickname);
    Optional<Account> findUserByEmailAndBannedDateIsNull(String email); //비활성화 먹인 email은 영원히 벤
    Optional<Account> findAccountByAccountIdAndBannedDateIsNull(Long accountId);

    boolean existsAccountByEmailAndBannedDateIsNull(String email);
    @Query(value = "SELECT EXISTS(SELECT 1 FROM accounts WHERE nickname=:nickname AND deactivation_date is null )", nativeQuery = true)
    boolean existsByNicknameNative(String nickname);







}
