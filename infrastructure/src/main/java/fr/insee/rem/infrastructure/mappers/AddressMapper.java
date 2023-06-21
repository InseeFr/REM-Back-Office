package fr.insee.rem.infrastructure.mappers;

import fr.insee.rem.domain.dtos.AddressDto;
import fr.insee.rem.infrastructure.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = LocationHelpMapper.class)
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    Address dtoToEntity(AddressDto address);

    AddressDto entityToDto(Address address);
}
