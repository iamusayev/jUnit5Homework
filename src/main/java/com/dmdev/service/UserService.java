package com.dmdev.service;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.User;
import com.dmdev.exception.ValidationException;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.validator.CreateUserValidator;
import com.dmdev.validator.ValidationResult;
import java.util.Optional;
import lombok.SneakyThrows;

public class UserService {


    private final CreateUserValidator createUserValidator;
    private final UserDao userDao;
    private final CreateUserMapper createUserMapper;
    private final UserMapper userMapper;

    public UserService() {
        this(UserDao.getInstance(), CreateUserMapper.getInstance(), UserMapper.getInstance(), CreateUserValidator.getInstance());
    }

    private UserService(UserDao userDao, CreateUserMapper createUserMapper, UserMapper userMapper, CreateUserValidator createUserValidator) {
        this.userDao = userDao;
        this.createUserMapper = createUserMapper;
        this.userMapper = userMapper;
        this.createUserValidator = createUserValidator;
    }


    public Optional<UserDto> login(String email, String password) {
        return userDao.findByEmailAndPassword(email, password)
                .map(userMapper::map);
    }


    @SneakyThrows
    public UserDto create(CreateUserDto userDto) {
        ValidationResult validationResult = createUserValidator.validate(userDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        User userEntity = createUserMapper.map(userDto);
        userDao.save(userEntity);

        return userMapper.map(userEntity);
    }
}