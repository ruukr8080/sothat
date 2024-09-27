package com.ex.sothat.domain.account;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
@Builder
@DynamicUpdate // Entity update할때 원하는 데이터만 update하기
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "accounts")
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
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Authority> roles = new HashSet<>();

    public Account addRole(Authority role) {
        this.roles.add(role);
        return this;
    }
}
