package fr.insee.rem.infrastructure.mappers;

import fr.insee.rem.domain.dtos.OtherIdentifierDto;
import fr.insee.rem.infrastructure.entity.OtherIdentifier;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OtherIdentifierMapper {

    OtherIdentifierMapper INSTANCE = Mappers.getMapper(OtherIdentifierMapper.class);

    OtherIdentifier dtoToEntity(OtherIdentifierDto otherIdentifier);

    OtherIdentifierDto entityToDto(OtherIdentifier otherIdentifier);
}
