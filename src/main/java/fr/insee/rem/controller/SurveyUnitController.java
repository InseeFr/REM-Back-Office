package fr.insee.rem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.insee.rem.entities.Response;
import fr.insee.rem.exception.SampleNotFoundException;
import fr.insee.rem.exception.SurveyUnitNotFoundException;
import fr.insee.rem.service.SurveyUnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(path = "/survey-unit")
public class SurveyUnitController {

    @Autowired
    SurveyUnitService surveyUnitService;

    @Operation(summary = "Add SurveyUnit to Sample", responses = {
        @ApiResponse(responseCode = "200", description = "SurveysUnit successfully added"),
        @ApiResponse(responseCode = "404", description = "Sample or SurveyUnit Not Found")
    })
    @PostMapping(path = "/{surveyUnitId}/sample/{sampleId}")
    public ResponseEntity<Object> addSurveyUnitToSample(
        HttpServletRequest request,
        @PathVariable("surveyUnitId") final Long surveyUnitId,
        @PathVariable("sampleId") final Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException {
        log.info("POST Add SurveyUnit {} to Sample {}", surveyUnitId, sampleId);
        Response response = surveyUnitService.addSurveyUnitToSample(surveyUnitId, sampleId);
        log.info("POST /survey-unit/{surveyUnitId}/sample/{sampleId} resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Operation(summary = "Delete SurveyUnit", responses = {
        @ApiResponse(responseCode = "200", description = "SurveysUnit successfully deleted"),
        @ApiResponse(responseCode = "404", description = "SurveyUnit Not Found")
    })
    @DeleteMapping(path = "/{surveyUnitId}")
    public ResponseEntity<Object> deleteSurveyUnitToSample(
        HttpServletRequest request,
        @PathVariable("surveyUnitId") final Long surveyUnitId) throws SurveyUnitNotFoundException {
        log.info("DELETE SurveyUnit {} ", surveyUnitId);
        Response response = surveyUnitService.deleteSurveyUnit(surveyUnitId);
        log.info("DELETE /survey-unit/{surveyUnitId} resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Operation(summary = "Remove SurveyUnit from Sample", responses = {
        @ApiResponse(responseCode = "200", description = "SurveysUnit successfully added"),
        @ApiResponse(responseCode = "404", description = "Sample or SurveyUnit Not Found")
    })
    @DeleteMapping(path = "/{surveyUnitId}/sample/{sampleId}")
    public ResponseEntity<Object> removeSurveyUnitFromSample(
        HttpServletRequest request,
        @PathVariable("surveyUnitId") final Long surveyUnitId,
        @PathVariable("sampleId") final Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException {
        log.info("DELTE Remove SurveyUnit {} from Sample {}", surveyUnitId, sampleId);
        Response response = surveyUnitService.removeSurveyUnitFromSample(surveyUnitId, sampleId);
        log.info("DELETE /survey-unit/{surveyUnitId}/sample/{sampleId} resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

}
