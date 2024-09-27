package com.ex.sothat.domain.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninRequest {
    private String email;
    private String password;
}