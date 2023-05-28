package com.dmdev.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.User;
import com.dmdev.util.LocalDateFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CreateUserMapperIT {

    private final CreateUserMapper createUserMapper = CreateUserMapper.getInstance();

    @Test
    void mapCreateUserDtoToEntity() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("test")
                .birthday("2020-05-05")
                .email("test@gmail.com")
                .password("test")
                .gender("MALE")
                .role("ADMIN")
                .build();
        User userEntity = createUserMapper.map(createUserDto);

        Assertions.assertAll(
                () -> assertThat(createUserDto).isNotNull(),
                () -> assertThat(userEntity).isNotNull(),
                () -> assertThat(createUserDto.getName()).isEqualTo(userEntity.getName()),
                () -> assertThat(LocalDateFormatter.format(createUserDto.getBirthday())).isEqualTo(userEntity.getBirthday()),
                () -> assertThat(createUserDto.getEmail()).isEqualTo(userEntity.getEmail()),
                () -> assertThat(createUserDto.getPassword()).isEqualTo(userEntity.getPassword()),
                () -> assertThat(createUserDto.getRole()).isEqualTo(userEntity.getRole().name()),
                () -> assertThat(createUserDto.getGender()).isEqualTo(userEntity.getGender().name())
        );
    }
}
