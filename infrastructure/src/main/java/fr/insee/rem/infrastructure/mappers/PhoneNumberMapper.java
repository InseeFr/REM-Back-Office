package fr.insee.rem.infrastructure.mappers;

import fr.insee.rem.domain.dtos.PhoneNumberDto;
import fr.insee.rem.infrastructure.entity.PhoneNumber;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PhoneNumberMapper {

    PhoneNumberMapper INSTANCE = Mappers.getMapper(PhoneNumberMapper.class);

    PhoneNumberDto entityToDto(PhoneNumber number);

    PhoneNumber dtoToEntity(PhoneNumberDto number);

    List<PhoneNumberDto> listEntityToListDto(List<PhoneNumber> numbers);

    List<PhoneNumber> listDtoToListEntity(List<PhoneNumberDto> numbers);
}
