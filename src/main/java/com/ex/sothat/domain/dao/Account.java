package com.ex.sothat.domain.dao;

import com.ex.sothat.domain.dto.AccountDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@DynamicUpdate // Entity update할때 원하는 데이터만 update하기
@NoArgsConstructor
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private String email; //로긴할 때 쓸 id
    private LocalDate birthdate;
    private String nickname; //사용자의 이름
    private String oauthType; // 사용자가 로그인한 서비스 (ex) google, naver..)
    private String userRole;
    private LocalDateTime signupDate; //가입일

    @Column(name = "profile_s3_key")
    private String profileS3Key; // 유저가 파일 업로드 할때 빌려줌. 권한 만료 설정 같은거 해줘야되나..?
    private LocalDateTime bannedDate; // 벤먹인 날짜. 이거로 벤 검증에도 쓸거임
    private LocalDateTime firstLoginDate; //사용자 이용 분석,통계할 때 쓸거
    private LocalDateTime lastLoginDate; // 사용자 이용 분석,통계할 때 쓸거

    public static AccountDto mapToDTO(Account account) {
        return AccountDto.builder()
                .accountId(account.accountId)
                .email(account.email)
                .birthdate(account.birthdate)
                .nickname(account.nickname)
                .profileS3Key(account.profileS3Key)
                .nickname(account.nickname)
                .oauthType(account.oauthType)
                .signupDate(account.signupDate)
                .build();
    }
}
