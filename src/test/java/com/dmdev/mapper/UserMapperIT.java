package com.dmdev.mapper;

import static com.dmdev.integration.IntegrationTestBase.USER_WITH_FIRST_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.dmdev.dto.UserDto;
import com.dmdev.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserMapperIT {

    private UserMapper userMapper = UserMapper.getInstance();

    @Test
    void mapUserEntityToDtoTest() {
        User userEntity = USER_WITH_FIRST_ID;

        UserDto userDto = userMapper.map(userEntity);

        Assertions.assertAll(
                () -> assertThat(userEntity).isNotNull(),
                () -> assertThat(userDto).isNotNull(),
                () -> assertThat(userEntity.getName()).isEqualTo(userDto.getName()),
                () -> assertThat(userEntity.getBirthday()).isEqualTo(userDto.getBirthday()),
                () -> assertThat(userEntity.getEmail()).isEqualTo(userDto.getEmail()),
                () -> assertThat(userEntity.getRole()).isEqualTo(userDto.getRole()),
                () -> assertThat(userEntity.getGender()).isEqualTo(userDto.getGender())
        );
    }
}
