package com.ex.sothat.domain.api;

import com.ex.sothat.domain.dto.LoginRequest;
import com.ex.sothat.domain.dto.ReissueRequest;
import com.ex.sothat.domain.dto.SignupRequest;
import com.ex.sothat.domain.dto.TokenRequest;
import com.ex.sothat.service.AuthService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/home")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenRequest> login(@RequestBody LoginRequest request) {
        TokenRequest token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/reissue") //재발행 할 때 쓸건데 따로 EX
    public ResponseEntity<TokenRequest> reissue(@RequestBody ReissueRequest request) {
        return ResponseEntity.ok(authService.reissue(request));
    }


}
