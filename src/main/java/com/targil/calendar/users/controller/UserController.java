package com.targil.calendar.users.controller;

import com.targil.calendar.users.model.dto.UserCreateDTO;
import com.targil.calendar.users.model.entity.UserEntity;
import com.targil.calendar.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        UserEntity user = userService.registerUser(userCreateDTO);
        return new ResponseEntity<>("User registered successfully. ID: " + user.getId(), HttpStatus.CREATED);
    }

}
