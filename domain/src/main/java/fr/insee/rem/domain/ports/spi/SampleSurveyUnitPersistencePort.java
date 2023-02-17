package fr.insee.rem.domain.ports.spi;

import java.util.List;

import fr.insee.rem.domain.dtos.SampleSurveyUnitDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;

public interface SampleSurveyUnitPersistencePort {

    List<Long> findAllIdsBySampleId(Long sampleId);

    List<SampleSurveyUnitDto> saveAll(Long sampleId, List<SurveyUnitDto> suList);

    SampleSurveyUnitDto addSurveyUnitToSample(Long surveyUnitId, Long sampleId);

    void removeSurveyUnitFromSample(Long surveyUnitId, Long sampleId);

    List<SampleSurveyUnitDto> findSurveyUnitsBySampleId(Long sampleId);

}
