1. JwtAccessDeniedHandler.java

이 놈은 권한 없는 놈들 403 에러 처리하는 핸들러임. 
접근 거부당한 놈들한테 "Access Denied" 메시지 던져주는 거임


2. JwtAuthenticationEntryPoint.java

얘는 인증 실패한 놈들 401 에러 처리하는 핸들러임. 
인증 안 된 놈들한테 "Unauthorized" 메시지 던져주는 거임


3. JwtFilter.java

ㄹㅇ 핵심 필터임. 요청 올 때마다 JWT 토큰 검증하고 인증 정보 설정해주는 놈임.
토큰 있고 유효하면 인증 정보 SecurityContext에 넣어주는 거임


4. JwtSecurityConfig.java

Spring Security 설정 클래스임. 
JwtFilter를 UsernamePasswordAuthenticationFilter 전에 추가해주는 역할함


5. JwtTokenProvider.java

JWT 토큰 생성, 검증, 파싱 다 하는 핵심 클래스임. 
토큰 만들고 유효성 검사하고 인증 정보 뽑아내는 등 JWT 관련 모든 일 처리하는 놈임