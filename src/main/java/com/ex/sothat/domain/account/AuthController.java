package com.ex.sothat.domain.account;

import com.ex.sothat.global.common.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    @Operation(summary = "Sign up", description = "Sign up a new user")
    public ResponseEntity<ResponseDto> signup(@RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseDto.success();
    }

    @PostMapping("/signin")
    @Operation(summary = "Sign in", description = "Sign in and get JWT token")
    public ResponseEntity<TokenResponse> signin(@RequestBody SigninRequest request) {
        TokenResponse token = authService.signin(request);
        return ResponseEntity.ok(token);
    }

}
