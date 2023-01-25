package fr.insee.rem.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import fr.insee.rem.domain.dtos.OtherIdentifierDto;
import fr.insee.rem.infrastructure.entity.OtherIdentifier;

@Mapper
public interface OtherIdentifierMapper {

    OtherIdentifierMapper INSTANCE = Mappers.getMapper(OtherIdentifierMapper.class);

    OtherIdentifier dtoToEntity(OtherIdentifierDto otherDto);

    OtherIdentifierDto entityToDto(OtherIdentifier other);
}
