package com.sparta.hanghaeboard.service;


import com.sparta.hanghaeboard.dto.LoginRequestDto;
import com.sparta.hanghaeboard.dto.ResponseDto;
import com.sparta.hanghaeboard.dto.SignupRequestDto;
import com.sparta.hanghaeboard.entity.User;
import com.sparta.hanghaeboard.entity.UserRoleEnum;
import com.sparta.hanghaeboard.jwt.JwtUtil;
import com.sparta.hanghaeboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

        private final UserRepository userRepository;
        private final JwtUtil jwtUtil;

        private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

        //**회원가입**
        @Transactional
        public ResponseDto signup(SignupRequestDto signupRequestDto) {
                String username = signupRequestDto.getUsername();
                String password = signupRequestDto.getPassword();

                // 회원 중복 확인
                Optional<User> found = userRepository.findByUsername(username);
                if (found.isPresent()) {
                        throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
                }

                UserRoleEnum role = UserRoleEnum.USER;
                if(signupRequestDto.isAdmin()) {
                        if(! signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
                        }
                        role = UserRoleEnum.ADMIN;
                }

                System.out.println("role = " + role);

                User user = new User(username, password, role);
                userRepository.save(user);

                return new ResponseDto("회원가입 성공", HttpStatus.OK.value());
        }

        //**로그인**
        @Transactional(readOnly = true)
        public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
                String username = loginRequestDto.getUsername();
                String password = loginRequestDto.getPassword();

                // 사용자 확인
                User user = userRepository.findByUsername(username).orElseThrow(
                        () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
                );

                // 비밀번호 확인
                if(!user.getPassword().equals(password)){
                        throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
                }

                // 로그인 확인되면 response의 헤더에 토큰 추가해주기
                response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        }
        }

