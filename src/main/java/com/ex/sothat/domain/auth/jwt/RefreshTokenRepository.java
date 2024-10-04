package com.ex.sothat.domain.auth.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    @Transactional
    void deleteByMemberId(String memberId); //회원 탈퇴

    @Transactional
    void deleteByRefreshToken(String refreshToken); //로그아웃

    Optional<RefreshToken> findByRefreshToken(String refresh);

}
