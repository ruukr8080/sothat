package com.ex.sothat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Getter
@Setter
@Builder
@DynamicUpdate // Entity update시, 원하는 데이터만 update하기
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(name = "email", nullable = false)
    private String email; // 로그인한 사용자의 이메일
    @Column(name = "name", nullable = false)
    private String name; // 로그인한 사용자의 이름
    @Column(name = "provider", nullable = false)
    private String provider; // 사용자가 로그인한 서비스(ex) google, naver..)
    private String providerId;// 사용자가 로그인한 서비스의 고유 ID


    public Member updateUser(String name, String email) {
        this.name = name;
        this.email = email;
        return this;
    }
}
