package fr.insee.rem.controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import fr.insee.rem.dto.SampleDto;
import fr.insee.rem.dto.SurveyUnitDto;
import fr.insee.rem.entities.Response;
import fr.insee.rem.exception.CsvFileException;
import fr.insee.rem.exception.SampleAlreadyExistsException;
import fr.insee.rem.exception.SampleNotFoundException;
import fr.insee.rem.service.SampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(path = "/sample")
public class SampleController {

    @Autowired
    SampleService sampleService;

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
        Response response = sampleService.addSampleFromCSVFile(sampleId, sampleFile);
        log.info("POST /sample/{sampleId}/addFromCSVFile resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Operation(summary = "Delete sample", responses = {
        @ApiResponse(responseCode = "200", description = "Sample successfully deleted"), @ApiResponse(responseCode = "404", description = "Sample Not Found")
    })
    @DeleteMapping(path = "/{sampleId}")
    public ResponseEntity<Object> deleteSample(HttpServletRequest request, @PathVariable("sampleId") final Long sampleId) throws SampleNotFoundException {
        log.info("DELETE sample {}", sampleId);
        Response response = sampleService.deleteSample(sampleId);
        log.info("DELETE /sample/{sampleId} resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
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
        return new ResponseEntity<>(sampleService.getSurveyUnitsBySample(sampleId), HttpStatus.OK);
    }

    @Operation(summary = "Create sample", responses = {
        @ApiResponse(responseCode = "200", description = "Sample successfully created"),
        @ApiResponse(responseCode = "409", description = "Sample Already Exists")
    })
    @PutMapping(path = "/create", consumes = {
        MediaType.TEXT_PLAIN_VALUE
    })
    public ResponseEntity<SampleDto> putSample(HttpServletRequest request, @RequestBody String label) throws SampleAlreadyExistsException {
        log.info("PUT create sample {}", label);
        return new ResponseEntity<>(sampleService.putSample(label), HttpStatus.OK);
    }

    @Operation(summary = "Get sample", responses = {
        @ApiResponse(responseCode = "200", description = "Sample successfully recovered"), @ApiResponse(responseCode = "404", description = "Sample Not Found")
    })
    @GetMapping(path = "/{sampleId}")
    public ResponseEntity<SampleDto> getSample(HttpServletRequest request, @PathVariable("sampleId") final Long sampleId) throws SampleNotFoundException {
        log.info("Get sample {}", sampleId);
        return new ResponseEntity<>(sampleService.getSample(sampleId), HttpStatus.OK);
    }

    @Operation(summary = "Get all samples", responses = {
        @ApiResponse(responseCode = "200", description = "Samples successfully recovered")
    })
    @GetMapping(path = "/all")
    public ResponseEntity<List<SampleDto>> getAllSamples(HttpServletRequest request) {
        log.info("Get all samples");
        return new ResponseEntity<>(sampleService.getAllSamples(), HttpStatus.OK);
    }
}
