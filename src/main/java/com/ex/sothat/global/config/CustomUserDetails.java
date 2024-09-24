package com.ex.sothat.global.config;

import com.ex.sothat.domain.dao.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private final Account account;

    public CustomUserDetails(Account account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> account.getUserRole());
    }

    @Override
    public String getPassword() {
        return null; // 비밀번호가 없으므로 null 반환
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    } //userName details의 userName임. null 처리 해야되나

    @Override
    public boolean isAccountNonExpired() {
        return true;
    } //토큰 인증인가 이거로 설정할까..?

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }// 이거 디폴트로 박아...?

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    } // 토큰만료 에러 메세지에서 쓸까

    @Override
    public boolean isEnabled() {
        return true;
    } //안쓰임 -> 유저 벤 먹인거 확인을 이거로...?

    public String getNickname() {
        return account.getNickname();
    }

    public Account getAccount() {
        return this.account;
    }
}
