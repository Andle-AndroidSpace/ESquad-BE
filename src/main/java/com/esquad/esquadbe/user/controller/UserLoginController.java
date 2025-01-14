package com.esquad.esquadbe.user.controller;

import com.esquad.esquadbe.user.dto.UserLoginRequestDTO;
import com.esquad.esquadbe.security.dto.RefreshTokenRequestDTO;
import com.esquad.esquadbe.global.api.ApiResponseEntity;
import com.esquad.esquadbe.security.jwt.JwtUtil;
import com.esquad.esquadbe.security.jwt.RefreshTokenService;
import com.esquad.esquadbe.user.service.UserGetService;
import com.esquad.esquadbe.user.service.UserLoginService;


import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserLoginController {

    private final UserLoginService loginService;
    private final UserGetService userGetService;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("login")
    public ResponseEntity<ApiResponseEntity> login(@RequestBody @Valid UserLoginRequestDTO loginRequestDTO) {
        // login 체크 후 token 생성
        var loginInfo = loginService.login(loginRequestDTO);

        return ApiResponseEntity.successResponseEntity(loginInfo);
    }

    @GetMapping("/inquiry")
    public ResponseEntity<ApiResponseEntity> dashboard(Authentication authentication) {
        // 사용자 정보 조회
        var result = userGetService.getUserById(JwtUtil.getLoginId(authentication));

        return ApiResponseEntity.successResponseEntity(result);
    }

    @PostMapping("/token-refresh")
    public ResponseEntity<ApiResponseEntity> tokenRefresh(@RequestBody @Valid RefreshTokenRequestDTO refreshTokenRequestDTO) {
        // token 재발급
        var result = refreshTokenService.refreshToken(refreshTokenRequestDTO.getRefreshToken());

        return ApiResponseEntity.successResponseEntity(result);
    }



}
