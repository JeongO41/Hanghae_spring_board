package com.sparta.hanghaeboard.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Setter
@Getter
public class SignupRequestDto {

    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "username은 알파벳 소문자와 숫자로 구성된 4~10자 입니다")
    private String username;
    @Pattern(regexp = "^[A-Za-z0-9]{8,15}$", message = "password는 알파벳 대소문자와 숫자로 구성된 8~15자 입니다")
    private String password;

    private boolean admin = false;
    private String adminToken = "";
}
