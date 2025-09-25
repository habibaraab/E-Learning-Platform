package com.spring.E_Learning.DTOs.Mapper;

import com.spring.E_Learning.DTOs.OptionDto;
import com.spring.E_Learning.Model.Option;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OptionMapper {
    OptionDto optionToDto(Option option);
    Option toEntity(OptionDto optionDto);
}
