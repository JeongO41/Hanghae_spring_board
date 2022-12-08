package com.sparta.hanghaeboard.controller;

import com.sparta.hanghaeboard.dto.LoginRequestDto;
import com.sparta.hanghaeboard.dto.ResponseDto;
import com.sparta.hanghaeboard.dto.SignupRequestDto;
import com.sparta.hanghaeboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public ModelAndView signupPage() {
        return new ModelAndView("signup");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }


    //**회원가입**  username, pw 클라이언트에서 전달받기
    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
       return userService.signup(signupRequestDto);

    }


    //**로그인** username, pw 클라이언트에서 전달받고 JWT토큰 쿠키에 저장
    @ResponseBody
    @PostMapping("/login")
    public ResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return new ResponseDto("로그인 성공", HttpStatus.OK.value());
    }

}