package fr.insee.rem.service;

import fr.insee.rem.entities.Response;
import fr.insee.rem.exception.SampleNotFoundException;
import fr.insee.rem.exception.SurveyUnitNotFoundException;

public interface SurveyUnitService {

    Response addSurveyUnitToSample(Long surveyUnitId, Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException;

    Response deleteSurveyUnit(Long surveyUnitId) throws SurveyUnitNotFoundException;

    Response removeSurveyUnitFromSample(Long surveyUnitId, Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException;

}
