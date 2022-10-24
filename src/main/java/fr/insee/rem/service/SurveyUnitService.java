package fr.insee.rem.service;

import java.util.List;

import fr.insee.rem.entities.Response;
import fr.insee.rem.exception.SampleNotFoundException;
import fr.insee.rem.exception.SurveyUnitNotFoundException;

public interface SurveyUnitService {

    Response addSurveyUnitToSample(Long surveyUnitId, Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException;

    Response addSurveyUnitsToSample(List<Long> surveyUnitIds, Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException;

}
