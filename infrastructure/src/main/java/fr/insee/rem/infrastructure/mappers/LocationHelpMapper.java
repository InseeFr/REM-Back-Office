package fr.insee.rem.infrastructure.mappers;

import fr.insee.rem.domain.dtos.LocationHelpDto;
import fr.insee.rem.infrastructure.entity.LocationHelp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationHelpMapper {

    LocationHelpMapper INSTANCE = Mappers.getMapper(LocationHelpMapper.class);

    @Mapping(source = "gpsCoordinates.latitude", target = "latitude")
    @Mapping(source = "gpsCoordinates.longitude", target = "longitude")
    LocationHelp dtoToEntity(LocationHelpDto locationHelp);

    @Mapping(target = "gpsCoordinates.latitude", source = "latitude")
    @Mapping(target = "gpsCoordinates.longitude", source = "longitude")
    LocationHelpDto entityToDto(LocationHelp locationHelp);
}
