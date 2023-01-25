package fr.insee.rem.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.infrastructure.entity.SurveyUnit;

@Mapper(uses = {
    AddressMapper.class, PersonMapper.class, OtherIdentifierMapper.class, AdditionalInformationMapper.class
})
public interface SurveyUnitMapper {

    SurveyUnitMapper INSTANCE = Mappers.getMapper(SurveyUnitMapper.class);

    @Mapping(target = "sampleSurveyUnits", ignore = true)
    @Mapping(source = "address", target = "surveyUnitData.address")
    @Mapping(source = "persons", target = "surveyUnitData.persons")
    @Mapping(source = "otherIdentifier", target = "surveyUnitData.otherIdentifier")
    @Mapping(source = "additionalInformations", target = "surveyUnitData.additionalInformations")
    SurveyUnit dtoToEntity(SurveyUnitDto surveyUnitDto);

    @Mapping(target = "address", source = "surveyUnitData.address")
    @Mapping(target = "persons", source = "surveyUnitData.persons")
    @Mapping(target = "otherIdentifier", source = "surveyUnitData.otherIdentifier")
    @Mapping(target = "additionalInformations", source = "surveyUnitData.additionalInformations")
    SurveyUnitDto entityToDto(SurveyUnit surveyUnit);

    List<SurveyUnitDto> listEntityToListDto(List<SurveyUnit> surveyUnits);

    List<SurveyUnit> listDtoToListEntity(List<SurveyUnitDto> surveyUnitsDto);
}
