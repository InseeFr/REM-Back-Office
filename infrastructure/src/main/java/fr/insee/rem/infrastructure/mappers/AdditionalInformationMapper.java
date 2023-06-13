package fr.insee.rem.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import fr.insee.rem.domain.dtos.AdditionalInformationDto;
import fr.insee.rem.infrastructure.entity.AdditionalInformation;

@Mapper
public interface AdditionalInformationMapper {

    AdditionalInformationMapper INSTANCE = Mappers.getMapper(AdditionalInformationMapper.class);

    AdditionalInformationDto entityToDto(AdditionalInformation addInfo);

    AdditionalInformation dtoToentity(AdditionalInformationDto addInfoDto);

    List<AdditionalInformationDto> listEntityToListDto(List<AdditionalInformation> addInfos);

    List<AdditionalInformation> listDtoToListEntity(List<AdditionalInformationDto> addInfosDto);
}
