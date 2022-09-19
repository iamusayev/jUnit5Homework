package com.dmdev.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.integration.IntegrationTestBase;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UserDaoIT extends IntegrationTestBase {

    private final UserDao userDao = UserDao.getInstance();

    @Test
    void getAllIsNotEmptyIfUsersExist() {
        List<User> users = userDao.findAll();
        assertThat(users).isNotEmpty();
    }

    @Test
    void getByIdIfUserExist() {
        Optional<User> maybeUser = userDao.findById(1);
        assertThat(maybeUser).isPresent();
    }

    @Test
    void getByIdIfUserNotExist() {
        Optional<User> maybeUser = userDao.findById(Integer.MAX_VALUE);
        assertThat(maybeUser).isEmpty();
    }


    @ParameterizedTest
    @MethodSource("getParametersForEmailAndPassword")
    void getByEmailAndPasswordTest(String email, String password, Optional<User> user) {
        Optional<User> maybeUser = userDao.findByEmailAndPassword(email, password);

        assertThat(maybeUser).isEqualTo(user);
    }

    static Stream<Arguments> getParametersForEmailAndPassword() {
        return Stream.of(
                Arguments.of("ivan@gmail.com", "111", Optional.of(USER_WITH_FIRST_ID)),
                Arguments.of("dummy", "111", Optional.empty()),
                Arguments.of("ivan@gmail.com", null, Optional.empty()),
                Arguments.of(null, "111", Optional.empty()),
                Arguments.of(null, null, Optional.empty()));
    }

    @Test
    void saveTest() {
        User user = User.builder()
                .name("test")
                .email("hey@gmail.com")
                .password("111")
                .role(Role.ADMIN)
                .gender(Gender.MALE)
                .birthday(LocalDate.now())
                .build();

        User save = userDao.save(user);

        assertThat(save.getId()).isNotNull();
    }

    @Test
    void deleteIfUserExist() {
        boolean deleteResult = userDao.delete(1);
        Optional<User> maybeUser = userDao.findById(1);

        Assertions.assertAll(
                () -> assertThat(deleteResult).isTrue(),
                () -> assertThat(maybeUser).isEmpty()
        );
    }

    @Test
    void deleteIfUserIsNotExist() {
        boolean deleteResult = userDao.delete(Integer.MAX_VALUE);
        assertThat(deleteResult).isFalse();
    }


    @Test
    void updateTest() {
        User userEntity = User.builder()
                .name("test")
                .password("test123")
                .birthday(LocalDate.now())
                .gender(Gender.FEMALE)
                .role(Role.USER)
                .email("test@gmail.com")
                .build();

        userDao.update(userEntity);
        Optional<User> updatedUser = userDao.findById(userEntity.getId());

        Assertions.assertAll(
                () -> updatedUser.ifPresent(user -> assertThat(user.getId()).isEqualTo(USER_WITH_FIRST_ID.getId())),
                () -> updatedUser.ifPresent(user -> assertThat(user.getName()).isNotEqualTo(USER_WITH_FIRST_ID.getName())),
                () -> updatedUser.ifPresent(user -> assertThat(user.getBirthday()).isNotEqualTo(USER_WITH_FIRST_ID.getBirthday())),
                () -> updatedUser.ifPresent(user -> assertThat(user.getGender()).isNotEqualTo(USER_WITH_FIRST_ID.getGender())),
                () -> updatedUser.ifPresent(user -> assertThat(user.getRole()).isNotEqualTo(USER_WITH_FIRST_ID.getRole())),
                () -> updatedUser.ifPresent(user -> assertThat(user.getEmail()).isNotEqualTo(USER_WITH_FIRST_ID.getEmail()))
        );
    }

//    @Test
//    void updateTestIf
}
