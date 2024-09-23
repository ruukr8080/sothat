1. JwtAccessDeniedHandler.java

이 놈은 권한 없는 애들 403 에러 처리하는 핸들러임. 
접근 거부당한 사용자한테 보내는 에러 안내. "Access Denied" 메세지 던짐.


2. JwtAuthenticationEntryPoint.java

얘는 인증 실패한 놈들 401 에러 처리하는 핸들러임. 
인증 안 된 사ㅏ용자는 "Unauthorized" 메시지 던짐


3. JwtFilter.java 
**ㄹㅇ 핵심 필터.** 

요청 올 때마다 JWT 토큰 검증하고 인증 정보 설정해주는 놈임.
토큰 있고 유효하면 인증 정보 SecurityContext에 넣어주는 거임


4. JwtSecurityConfig.java

Spring Security 설정 클래스임. 
JwtFilter를 UsernamePasswordAuthenticationFilter 전에 추가해주는 역할함


5. JwtTokenProvider.java

JWT 토큰 생성, 검증, 파싱 다 하는 핵심 클래스임. 
토큰 만들고 유효성 검사하고 인증 정보 뽑아내는 등 JWT 관련 모든 일 처리하는 놈임



---
## 토큰 만료 이슈

토큰이 만료될거란 생각을 못하고 계속 삽질함.
-> 자동발행 방법  2가지 찾음.

1. 인터셉터나 필터를 사용하여 모든 요청을 가로채고, 액세스 토큰이 만료되었을 때 자동으로 재발행을 시도하는 방법.
2. 클라이언트 측에서 액세스 토큰 만료 시간을 추적하고, 만료 직전에 자동으로 재발행을 요청하는 방법.


---
