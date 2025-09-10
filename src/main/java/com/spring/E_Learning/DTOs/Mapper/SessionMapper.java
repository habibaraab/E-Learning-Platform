package com.spring.E_Learning.DTOs.Mapper;

import com.spring.E_Learning.DTOs.SessionRequestDto;
import com.spring.E_Learning.DTOs.SessionResponseDto;
import com.spring.E_Learning.Model.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    @Mapping(source = "course.title", target = "courseTitle")
    SessionResponseDto toDto(Session session);

    @Mapping(target = "course", ignore = true)
    Session  toEntity(SessionRequestDto sessionRequestDto);
}
