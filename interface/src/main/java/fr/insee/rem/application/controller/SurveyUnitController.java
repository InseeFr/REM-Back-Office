package fr.insee.rem.application.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import fr.insee.rem.application.adapter.HouseholdCsvAdapter;
import fr.insee.rem.application.response.Response;
import fr.insee.rem.application.sources.HouseholdCsvSource;
import fr.insee.rem.application.utils.CsvToBeanUtils;
import fr.insee.rem.domain.dtos.SampleSurveyUnitDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.exception.CsvFileException;
import fr.insee.rem.domain.exception.SampleNotFoundException;
import fr.insee.rem.domain.exception.SurveyUnitNotFoundException;
import fr.insee.rem.domain.ports.api.SurveyUnitServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(path = "/survey-unit")
public class SurveyUnitController {

    @Autowired
    SurveyUnitServicePort surveyUnitService;

    @Autowired
    HouseholdCsvAdapter householdCsvAdapter;

    @Operation(summary = "Add SurveyUnits from CSV file to Sample", responses = {
        @ApiResponse(responseCode = "200", description = "SurveysUnits successfully added"), @ApiResponse(responseCode = "400", description = "CSV File Error"),
        @ApiResponse(responseCode = "404", description = "Sample Not Found")
    })
    @PostMapping(path = "/{sampleId}/addFromCSVFile", consumes = {
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<Object> addSampleFromCSVFile(
        HttpServletRequest request,
        @PathVariable("sampleId") final Long sampleId,
        @RequestPart("sample") MultipartFile sampleFile) throws SampleNotFoundException, CsvFileException {
        log.info("POST add sample {} from csv file {}", sampleId, sampleFile.getOriginalFilename());
        List<HouseholdCsvSource> householdCsvList = CsvToBeanUtils.parse(sampleFile, HouseholdCsvSource.class);
        List<SurveyUnitDto> surveyUnitDtos = new ArrayList<>();
        householdCsvList.forEach(h -> surveyUnitDtos.add(householdCsvAdapter.convert(h)));
        List<SampleSurveyUnitDto> ssuDtos = surveyUnitService.importSurveyUnitsToSample(sampleId, surveyUnitDtos);
        Response response = new Response(String.format("%s surveyUnits created", ssuDtos.size()), HttpStatus.OK);
        log.info("POST /sample/{sampleId}/addFromCSVFile resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

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
        SampleSurveyUnitDto ssuDto = surveyUnitService.addSurveyUnitToSample(surveyUnitId, sampleId);
        Response response =
            new Response(String.format("SurveyUnit %s add to sample %s", ssuDto.getSurveyUnit().getRepositoryId(), ssuDto.getSample().getId()), HttpStatus.OK);
        log.info("POST /survey-unit/{surveyUnitId}/sample/{sampleId} resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());

        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Operation(summary = "Get SurveyUnit by Id", responses = {
        @ApiResponse(responseCode = "200", description = "SurveysUnit successfully found"),
        @ApiResponse(responseCode = "404", description = "SurveyUnit Not Found")
    })
    @GetMapping(path = "/{surveyUnitId}")
    public ResponseEntity<SurveyUnitDto> getSurveyUnit(
        HttpServletRequest request,
        @PathVariable("surveyUnitId") final Long surveyUnitId) throws SurveyUnitNotFoundException {
        log.info("GET SurveyUnit {}", surveyUnitId);
        return new ResponseEntity<>(surveyUnitService.getSurveyUnitById(surveyUnitId), HttpStatus.OK);
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
        surveyUnitService.deleteSurveyUnitById(surveyUnitId);
        Response response = new Response(String.format("SurveyUnit %s deleted", surveyUnitId), HttpStatus.OK);
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
        surveyUnitService.removeSurveyUnitFromSample(surveyUnitId, sampleId);
        Response response = new Response(String.format("SurveyUnit %s removed from sample %s", surveyUnitId, sampleId), HttpStatus.OK);
        log.info("DELETE /survey-unit/{surveyUnitId}/sample/{sampleId} resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Operation(summary = "Get SurveyUnits by Sample", responses = {
        @ApiResponse(responseCode = "200", description = "SurveysUnits successfully recovered"),
        @ApiResponse(responseCode = "404", description = "Sample Not Found")
    })
    @GetMapping(path = "/{sampleId}/survey-units", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SurveyUnitDto>> getSurveyUnitsBySample(
        HttpServletRequest request,
        @PathVariable("sampleId") final Long sampleId) throws SampleNotFoundException {
        log.info("Get SurveyUnits by Sample {}", sampleId);
        List<SurveyUnitDto> suList = surveyUnitService.getSurveyUnitsBySampleId(sampleId).stream().map(SampleSurveyUnitDto::getSurveyUnit).toList();
        return new ResponseEntity<>(suList, HttpStatus.OK);
    }

    @Operation(summary = "Get list of Survey Units Ids", responses = {
        @ApiResponse(responseCode = "200", description = "List of Survey Units Ids successfully recovered"),
        @ApiResponse(responseCode = "404", description = "Sample Not Found")
    })
    @GetMapping(path = "/{sampleId}/survey-units-ids")
    public ResponseEntity<List<Long>> getListOfIds(HttpServletRequest request, @PathVariable("sampleId") final Long sampleId) throws SampleNotFoundException {
        log.info("Get list of Survey Units Ids for sample {}", sampleId);
        return new ResponseEntity<>(surveyUnitService.getSurveyUnitIdsBySampleId(sampleId), HttpStatus.OK);
    }

}
