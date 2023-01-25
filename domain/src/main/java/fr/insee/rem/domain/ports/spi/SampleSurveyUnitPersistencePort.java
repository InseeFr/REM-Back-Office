package fr.insee.rem.domain.ports.spi;

import java.util.List;

import fr.insee.rem.domain.dtos.SampleDto;
import fr.insee.rem.domain.dtos.SampleSurveyUnitDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;

public interface SampleSurveyUnitPersistencePort {

    List<SampleSurveyUnitDto> findBySampleWithSurveyUnit(SampleDto sample);

    List<Long> findAllIdsBySampleId(Long sampleId);

    SampleSurveyUnitDto save(SampleSurveyUnitDto sampleSurveyUnitDto);

    void delete(SampleSurveyUnitDto sampleSurveyUnitDto);

    List<SampleSurveyUnitDto> saveAll(Long sampleId, List<SurveyUnitDto> suList);

}
