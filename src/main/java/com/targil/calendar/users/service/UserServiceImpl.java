package com.targil.calendar.users.service;

import com.targil.calendar.users.model.dto.UserCreateDTO;
import com.targil.calendar.users.model.entity.UserEntity;
import com.targil.calendar.users.model.exception.UserNotFoundException;
import com.targil.calendar.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserEntity registerUser(UserCreateDTO userCreateDTO) {
        if (userRepository.existsByEmail(userCreateDTO.getEmail())) {
            throw new UserNotFoundException("User not found");
        }
        UserEntity newUser = new UserEntity();
        newUser.setEmail(userCreateDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        return userRepository.save(newUser);
    }
}
