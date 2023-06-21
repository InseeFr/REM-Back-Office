package fr.insee.rem.infrastructure.mappers;

import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.infrastructure.entity.SurveyUnitEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {
        AddressMapper.class,
        PersonMapper.class,
        OtherIdentifierMapper.class,
        AdditionalInformationMapper.class
})
public interface SurveyUnitMapper {

    SurveyUnitMapper INSTANCE = Mappers.getMapper(SurveyUnitMapper.class);

    @Mapping(target = "partitionSurveyUnitLinkEntities", ignore = true)
    @Mapping(source = "address", target = "surveyUnitData.address")
    @Mapping(source = "persons", target = "surveyUnitData.persons")
    @Mapping(source = "otherIdentifier", target = "surveyUnitData.otherIdentifier")
    @Mapping(source = "additionalInformations", target = "surveyUnitData.additionalInformations")
    SurveyUnitEntity dtoToEntity(SurveyUnitDto surveyUnit);

    @Mapping(target = "address", source = "surveyUnitData.address")
    @Mapping(target = "persons", source = "surveyUnitData.persons")
    @Mapping(target = "otherIdentifier", source = "surveyUnitData.otherIdentifier")
    @Mapping(target = "additionalInformations", source = "surveyUnitData.additionalInformations")
    SurveyUnitDto entityToDto(SurveyUnitEntity surveyUnitEntity);

    List<SurveyUnitDto> listEntityToListDto(List<SurveyUnitEntity> surveyUnitEntities);

    List<SurveyUnitEntity> listDtoToListEntity(List<SurveyUnitDto> surveyUnits);
}
