package com.targil.calendar.users.service;

import com.targil.calendar.users.model.dto.UserCreateDTO;
import com.targil.calendar.users.model.entity.UserEntity;

public interface UserService {
    UserEntity registerUser(UserCreateDTO userCreateDTO);

}
