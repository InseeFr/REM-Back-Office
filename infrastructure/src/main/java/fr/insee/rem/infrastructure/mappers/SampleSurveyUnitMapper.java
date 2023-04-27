package fr.insee.rem.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import fr.insee.rem.domain.dtos.SampleSurveyUnitDto;
import fr.insee.rem.infrastructure.entity.SampleSurveyUnit;

@Mapper(uses = {
        SampleMapper.class,
        SurveyUnitMapper.class
})
public interface SampleSurveyUnitMapper {

    SampleSurveyUnitMapper INSTANCE = Mappers.getMapper(SampleSurveyUnitMapper.class);

    @Mapping(
            target = "id", expression = "java(new fr.insee.rem.infrastructure.entity.SampleSurveyUnitPK(sampleSurveyUnitDto.getSample().getId(), sampleSurveyUnitDto.getSurveyUnit().getRepositoryId()))")
    SampleSurveyUnit dtoToEntity(SampleSurveyUnitDto sampleSurveyUnitDto);

    SampleSurveyUnitDto entityToDto(SampleSurveyUnit sampleSurveyUnit);

    List<SampleSurveyUnitDto> listEntityToListDto(List<SampleSurveyUnit> sampleSurveyUnits);

    List<SampleSurveyUnit> listDtoToListEntity(List<SampleSurveyUnitDto> sampleSurveyUnitsDto);
}
