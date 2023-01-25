package fr.insee.rem.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import fr.insee.rem.domain.dtos.PhoneNumberDto;
import fr.insee.rem.infrastructure.entity.PhoneNumber;

@Mapper
public interface PhoneNumberMapper {

    PhoneNumberMapper INSTANCE = Mappers.getMapper(PhoneNumberMapper.class);

    PhoneNumberDto entityToDto(PhoneNumber numbers);

    PhoneNumber dtoToEntity(PhoneNumberDto numbersDto);

    List<PhoneNumberDto> listEntityToListDto(List<PhoneNumber> numbers);

    List<PhoneNumber> listDtoToListEntity(List<PhoneNumberDto> numbersDto);
}
