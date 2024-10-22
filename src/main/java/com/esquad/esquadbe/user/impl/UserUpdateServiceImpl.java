package com.esquad.esquadbe.user.impl;

import com.esquad.esquadbe.global.exception.custom.user.UserNotFoundException;
import com.esquad.esquadbe.user.dto.UserUpdateDTO;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
import com.esquad.esquadbe.user.service.UserUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUpdateServiceImpl implements UserUpdateService {
    private final UserRepository userRepository;

    @Override
    public User updateUser(Long userId, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        user = userUpdateDTO.toEntity(userUpdateDTO,user);

        return userRepository.save(user);
    }
}