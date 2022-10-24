package fr.insee.rem.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.rem.entities.Response;
import fr.insee.rem.exception.SampleNotFoundException;
import fr.insee.rem.exception.SurveyUnitNotFoundException;
import fr.insee.rem.service.SurveyUnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(path = "/survey-unit")
public class SurveyUnitController {

    @Autowired
    SurveyUnitService surveyUnitService;

    @Operation(summary = "Add SurveyUnit to Sample", responses = {
        @ApiResponse(responseCode = "200", description = "SurveysUnit successfully added"),
        @ApiResponse(responseCode = "404", description = "Sample or SurveyUnit Not Found")
    })
    @PostMapping(path = "{surveyUnitId}/sample/{sampleId}")
    public ResponseEntity<Object> addSurveyUnitToSample(
        HttpServletRequest request,
        @PathVariable("surveyUnitId") final Long surveyUnitId,
        @PathVariable("sampleId") final Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException {
        log.info("POST Add SurveyUnit {} to Sample {}", surveyUnitId, sampleId);
        Response response = surveyUnitService.addSurveyUnitToSample(surveyUnitId, sampleId);
        log.info("POST /survey-unit/{surveyUnitId}/sample/{sampleId} resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Operation(summary = "Add SurveyUnits to Sample", responses = {
        @ApiResponse(responseCode = "200", description = "SurveysUnits successfully added"),
        @ApiResponse(responseCode = "404", description = "Sample or SurveyUnit Not Found")
    })
    @PostMapping(path = "/sample/{sampleId}")
    public ResponseEntity<Object> addSurveyUnitsToSample(
        HttpServletRequest request,
        @PathVariable("sampleId") final Long sampleId,
        @RequestBody List<Long> surveyUnitIds) throws SampleNotFoundException, SurveyUnitNotFoundException {
        log.info("POST Add SurveyUnits to Sample {}", sampleId);
        Response response = surveyUnitService.addSurveyUnitsToSample(surveyUnitIds, sampleId);
        log.info("POST /survey-unit/sample/{sampleId} resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

}
