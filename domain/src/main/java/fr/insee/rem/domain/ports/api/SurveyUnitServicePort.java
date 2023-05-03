package fr.insee.rem.domain.ports.api;

import java.util.List;

import fr.insee.rem.domain.dtos.SampleSurveyUnitDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.exception.SampleNotFoundException;
import fr.insee.rem.domain.exception.SurveyUnitNotFoundException;
import fr.insee.rem.domain.exception.SurveyUnitsNotFoundException;
import fr.insee.rem.domain.records.SuIdMappingRecord;

public interface SurveyUnitServicePort {

    List<SampleSurveyUnitDto> importSurveyUnitsToSample(Long sampleId, List<SurveyUnitDto> suList) throws SampleNotFoundException;

    SampleSurveyUnitDto addSurveyUnitToSample(Long surveyUnitId, Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException;

    void deleteSurveyUnitById(Long surveyUnitId) throws SurveyUnitNotFoundException;

    void removeSurveyUnitFromSample(Long surveyUnitId, Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException;

    SurveyUnitDto getSurveyUnitById(Long surveyUnitId) throws SurveyUnitNotFoundException;

    List<SampleSurveyUnitDto> getSurveyUnitsBySampleId(Long sampleId) throws SampleNotFoundException;

    List<Long> getSurveyUnitIdsBySampleId(Long sampleId) throws SampleNotFoundException;

    List<SuIdMappingRecord> getIdMappingTableBySampleId(Long sampleId) throws SampleNotFoundException;

    int addSurveyUnitsToSample(List<Long> surveyUnitIds, Long sampleId) throws SampleNotFoundException, SurveyUnitsNotFoundException;

    boolean checkRepositoryId(List<SurveyUnitDto> surveyUnitDtos);

}
