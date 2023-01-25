package fr.insee.rem.domain.ports.api;

import java.util.List;

import fr.insee.rem.domain.dtos.SampleSurveyUnitDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.exception.SampleNotFoundException;
import fr.insee.rem.domain.exception.SurveyUnitNotFoundException;

public interface SurveyUnitServicePort {

    List<SampleSurveyUnitDto> importSurveyUnitsToSample(Long sampleId, List<SurveyUnitDto> suList) throws SampleNotFoundException;

    SampleSurveyUnitDto addSurveyUnitToSample(Long surveyUnitId, Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException;

    void deleteSurveyUnitById(Long surveyUnitId) throws SurveyUnitNotFoundException;

    void removeSurveyUnitFromSample(Long surveyUnitId, Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException;

    SurveyUnitDto getSurveyUnitById(Long surveyUnitId) throws SurveyUnitNotFoundException;

    List<SampleSurveyUnitDto> getSurveyUnitsBySampleId(Long sampleId) throws SampleNotFoundException;

    List<Long> getSurveyUnitIdsBySampleId(Long sampleId) throws SampleNotFoundException;

}
