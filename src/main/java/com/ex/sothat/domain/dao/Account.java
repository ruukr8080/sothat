package com.ex.sothat.domain.dao;

import com.ex.sothat.global.common.Authority;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.HashSet;
import java.util.Set;



@Getter
@Builder
@DynamicUpdate // Entity update할때 원하는 데이터만 update하기
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;
    @Column(name = "email", nullable = false)
    private String email; //로긴할 때 쓸 id
    private String password;
    @Column(name = "name", nullable = false)
    private String name; //사용자의 이름
    @Column(name = "provider", nullable = false)
    private String provider; // 사용자가 로그인한 서비스 (ex) google, naver..)
    private String providerId;// 사용자가 로그인한 서비스의 고유 ID. 핋요한가?
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Authority> roles = new HashSet<>();

    public Account updateUser(String name, String email) {
        this.name = name;
        this.email = email;
        return this;
    }

    public void addRole(Authority role) {
        this.roles.add(role);
    }
}
