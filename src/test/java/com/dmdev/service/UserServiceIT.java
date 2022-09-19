package com.dmdev.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.User;
import com.dmdev.exception.ValidationException;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.validator.CreateUserValidator;
import com.dmdev.validator.Error;
import com.dmdev.validator.ValidationResult;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserServiceIT extends IntegrationTestBase {

    private final CreateUserValidator createUserValidator = CreateUserValidator.getInstance();
    private final UserDao userDao = UserDao.getInstance();
    private final CreateUserMapper createUserMapper = CreateUserMapper.getInstance();
    private final UserMapper userMapper = UserMapper.getInstance();
    private final UserService userService = new UserService();


    @Test
    void loginSuccessIfUserExist() {
        Optional<User> maybeUser = userDao.findByEmailAndPassword(USER_WITH_FIRST_ID.getEmail(), USER_WITH_FIRST_ID.getPassword());
        Optional<UserDto> userDto = maybeUser.map(userMapper::map);
        assertAll(
                () -> assertThat(maybeUser).isNotEmpty(),
                () -> assertThat(userDto).isNotEmpty()
        );
    }

    @Test
    void loginFailIfUserNotExist() {
        Optional<UserDto> maybeUser = userService.login("Dummy", "Dummy");
        assertThat(maybeUser).isEmpty();
    }

    @Test
    void loginFailIfEmailIncorrect() {
        Optional<UserDto> maybeUser = userService.login("Dummy", USER_WITH_FIRST_ID.getPassword());
        assertThat(maybeUser).isEmpty();
    }

    @Test
    void loginFailIfPasswordIncorrect() {
        Optional<UserDto> maybeUser = userService.login(USER_WITH_FIRST_ID.getEmail(), "Dummy");
        assertThat(maybeUser).isEmpty();
    }

    @Test
    void createSuccessIfValidationResultIsEmpty() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("test")
                .birthday("2020-05-05")
                .email("test@gmail.com")
                .gender("MALE")
                .role("ADMIN")
                .password("test1337")
                .build();
        ValidationResult validationResult = createUserValidator.validate(createUserDto);
        boolean isValid = validationResult.isValid();
        assertThat(isValid).isTrue();
        User userEntity = createUserMapper.map(createUserDto);
        User savedUser = userDao.save(userEntity);
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    @DisplayName("Error, if format is not  (yyyy-MM-dd)")
    void createFailIfBirthdayIsIncorrect() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("test")
                .birthday("05-05-2020")
                .email("test@gmail.com")
                .gender("MALE")
                .role("ADMIN")
                .password("test1337")
                .build();
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.create(createUserDto));
        List<Error> errorList = exception.getErrors();
        assertThat(errorList).hasSize(1);
    }

    @Test
    @DisplayName("Error, if Gender is incorrect")
    void createFailIfGenderIsIncorrect() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("test")
                .birthday("2020-05-05")
                .email("test@gmail.com")
                .gender("MALEEE")
                .role("ADMIN")
                .password("test1337")
                .build();
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.create(createUserDto));
        List<Error> errorList = exception.getErrors();
        assertThat(errorList).hasSize(1);
    }

    @Test
    @DisplayName("Error, if Role is incorrect")
    void createFailIfRoleIsIncorrect() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("test")
                .birthday("2020-05-05")
                .email("test@gmail.com")
                .gender("MALE")
                .role("ADMINNNN")
                .password("test1337")
                .build();
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.create(createUserDto));
        List<Error> errorList = exception.getErrors();
        assertThat(errorList).hasSize(1);

    }
}
