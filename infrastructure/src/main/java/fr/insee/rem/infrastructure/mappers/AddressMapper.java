package fr.insee.rem.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import fr.insee.rem.domain.dtos.AddressDto;
import fr.insee.rem.infrastructure.entity.Address;

@Mapper(uses = LocationHelpMapper.class)
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    Address dtoToEntity(AddressDto addressDto);

    AddressDto entityToDto(Address address);
}
