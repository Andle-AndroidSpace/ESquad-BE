package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.exception.UserIdException;
import com.esquad.esquadbe.user.exception.UserNicknameException;
import com.esquad.esquadbe.user.dto.UserJoinDTO;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserJoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(UserJoinDTO userJoinDTO) {
        if (userRepository.existsByUsername(userJoinDTO.getUsername())) {
            throw new UserIdException("이미 존재하는 아이디입니다.");
        }

        if (userRepository.existsByNickname(userJoinDTO.getNickname())) {
            throw new UserNicknameException("이미 존재하는 닉네임입니다.");
        }

        if(userJoinDTO.getNickname().isEmpty()) {
            userJoinDTO.setDefaultNickname();
        }

        User user = userJoinDTO.toEntity(bCryptPasswordEncoder);

        userRepository.save(user);
    }

    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
