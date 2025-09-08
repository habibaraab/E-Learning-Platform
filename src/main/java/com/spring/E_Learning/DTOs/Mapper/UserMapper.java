package com.spring.E_Learning.DTOs.Mapper;


import com.spring.E_Learning.DTOs.UserRequestDto;
import com.spring.E_Learning.DTOs.UserResponseDto;
import com.spring.E_Learning.Model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toUserResponseDto(User user);


    User toUserEntity(UserRequestDto requestDto);

}
