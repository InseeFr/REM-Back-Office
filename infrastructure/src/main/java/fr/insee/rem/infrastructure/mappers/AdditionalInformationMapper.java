package fr.insee.rem.infrastructure.mappers;

import fr.insee.rem.domain.dtos.AdditionalInformationDto;
import fr.insee.rem.infrastructure.entity.AdditionalInformation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AdditionalInformationMapper {

    AdditionalInformationMapper INSTANCE = Mappers.getMapper(AdditionalInformationMapper.class);

    AdditionalInformationDto entityToDto(AdditionalInformation additionalInformation);

    AdditionalInformation dtoToEntity(AdditionalInformationDto additionalInformation);

    List<AdditionalInformationDto> listEntityToListDto(List<AdditionalInformation> additionalInformations);

    List<AdditionalInformation> listDtoToListEntity(List<AdditionalInformationDto> additionalInformations);
}
