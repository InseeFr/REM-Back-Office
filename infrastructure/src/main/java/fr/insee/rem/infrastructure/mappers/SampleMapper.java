package fr.insee.rem.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import fr.insee.rem.domain.dtos.SampleDto;
import fr.insee.rem.infrastructure.entity.Sample;

@Mapper
public interface SampleMapper {

    SampleMapper INSTANCE = Mappers.getMapper(SampleMapper.class);

    @Mapping(target = "sampleSurveyUnits", ignore = true)
    Sample dtoToEntity(SampleDto sampleDto);

    SampleDto entityToDto(Sample sample);

    List<SampleDto> listEntityToListDto(List<Sample> samples);

    List<Sample> listDtoToListEntity(List<SampleDto> samplesDto);
}
