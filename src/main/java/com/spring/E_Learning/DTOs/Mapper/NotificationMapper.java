package com.spring.E_Learning.DTOs.Mapper;


import com.spring.E_Learning.DTOs.NotificationDto;
import com.spring.E_Learning.Model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(source = "user.id", target = "userId")
    NotificationDto toDto(Notification notification);

    @Mapping(source = "userId", target = "user.id")
    Notification toEntity(NotificationDto dto);
}
