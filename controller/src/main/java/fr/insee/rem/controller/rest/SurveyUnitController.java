package fr.insee.rem.controller.rest;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
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

import fr.insee.rem.controller.adapter.HouseholdCsvAdapter;
import fr.insee.rem.controller.exception.CsvFileException;
import fr.insee.rem.controller.response.Response;
import fr.insee.rem.controller.sources.HouseholdCsvSource;
import fr.insee.rem.controller.targets.SuIdMappingCsvTarget;
import fr.insee.rem.controller.utils.BeanToCsvUtils;
import fr.insee.rem.controller.utils.CsvToBeanUtils;
import fr.insee.rem.domain.dtos.SampleSurveyUnitDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.exception.SampleNotFoundException;
import fr.insee.rem.domain.exception.SurveyUnitNotFoundException;
import fr.insee.rem.domain.exception.SurveyUnitsNotFoundException;
import fr.insee.rem.domain.ports.api.SurveyUnitServicePort;
import fr.insee.rem.domain.records.SuIdMappingRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@Tag(name = "SurveyUnits endpoints")
@RequestMapping(path = "/survey-units")
public class SurveyUnitController {

    @Autowired
    SurveyUnitServicePort surveyUnitService;

    @Autowired
    HouseholdCsvAdapter householdCsvAdapter;

    @Operation(summary = "Add Household SurveyUnits to Sample from CSV file", responses = {
        @ApiResponse(responseCode = "200", description = "SurveysUnits successfully added"), @ApiResponse(responseCode = "400", description = "CSV File Error"),
        @ApiResponse(responseCode = "404", description = "Sample Not Found")
    })
    @PostMapping(path = "/households/samples/{sampleId}/csv-upload", consumes = {
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<Object> addHouseholdSuFromCSVFile(
        @PathVariable("sampleId") final Long sampleId,
        @RequestPart("sample") MultipartFile sampleFile) throws SampleNotFoundException, CsvFileException {
        log.info("POST add sample {} from csv file {}", sampleId, sampleFile.getOriginalFilename());
        List<HouseholdCsvSource> householdCsvList = CsvToBeanUtils.parse(sampleFile, HouseholdCsvSource.class);
        List<SurveyUnitDto> surveyUnitDtos = householdCsvList.stream().map(h -> householdCsvAdapter.convert(h)).toList();
        List<SampleSurveyUnitDto> ssuDtos = surveyUnitService.importSurveyUnitsToSample(sampleId, surveyUnitDtos);
        Response response = new Response(String.format("%s surveyUnits created", ssuDtos.size()), HttpStatus.OK);
        log.info("POST /survey-units/samples/{sampleId} resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Operation(summary = "Add SurveyUnit to Sample", responses = {
        @ApiResponse(responseCode = "200", description = "SurveysUnit successfully added"),
        @ApiResponse(responseCode = "404", description = "Sample or SurveyUnit Not Found")
    })
    @PutMapping(path = "/{surveyUnitId}/samples/{sampleId}")
    public ResponseEntity<Object> addSurveyUnitToSample(
        @PathVariable("surveyUnitId") final Long surveyUnitId,
        @PathVariable("sampleId") final Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException {
        log.info("PUT Add SurveyUnit {} to Sample {}", surveyUnitId, sampleId);
        SampleSurveyUnitDto ssuDto = surveyUnitService.addSurveyUnitToSample(surveyUnitId, sampleId);
        Response response =
            new Response(String.format("SurveyUnit %s add to sample %s", ssuDto.getSurveyUnit().getRepositoryId(), ssuDto.getSample().getId()), HttpStatus.OK);
        log.info("PUT /survey-units/{surveyUnitId}/samples/{sampleId} resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());

        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Operation(summary = "Add SurveyUnits List to Sample", responses = {
        @ApiResponse(responseCode = "200", description = "SurveysUnit successfully added"),
        @ApiResponse(responseCode = "404", description = "Sample or SurveyUnit Not Found")
    })
    @PutMapping(path = "/samples/{sampleId}")
    public ResponseEntity<Object> addSurveyUnitsToSample(
        @RequestBody List<Long> surveyUnitIds,
        @PathVariable("sampleId") final Long sampleId) throws SampleNotFoundException, SurveyUnitsNotFoundException {
        log.info("PUT Add SurveyUnits List to Sample {}", sampleId);
        if (surveyUnitIds == null || surveyUnitIds.isEmpty()) {
            Response response = new Response("SurveyUnits List empty", HttpStatus.BAD_REQUEST);
            log.info("PUT /survey-units/samples/{sampleId} resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
            return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
        }
        int count = surveyUnitService.addSurveyUnitsToSample(surveyUnitIds, sampleId);
        Response response = new Response(String.format("%s SurveyUnits add to sample %s", count, sampleId), HttpStatus.OK);
        log.info("PUT /survey-units/samples/{sampleId} resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());

        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Operation(summary = "Get SurveyUnit by Id", responses = {
        @ApiResponse(responseCode = "200", description = "SurveysUnit successfully found"),
        @ApiResponse(responseCode = "404", description = "SurveyUnit Not Found")
    })
    @GetMapping(path = "/{surveyUnitId}")
    public ResponseEntity<SurveyUnitDto> getSurveyUnit(@PathVariable("surveyUnitId") final Long surveyUnitId) throws SurveyUnitNotFoundException {
        log.info("GET SurveyUnit {}", surveyUnitId);
        return new ResponseEntity<>(surveyUnitService.getSurveyUnitById(surveyUnitId), HttpStatus.OK);
    }

    @Operation(summary = "Delete SurveyUnit", responses = {
        @ApiResponse(responseCode = "200", description = "SurveysUnit successfully deleted"),
        @ApiResponse(responseCode = "404", description = "SurveyUnit Not Found")
    })
    @DeleteMapping(path = "/{surveyUnitId}")
    public ResponseEntity<Object> deleteSurveyUnitToSample(@PathVariable("surveyUnitId") final Long surveyUnitId) throws SurveyUnitNotFoundException {
        log.info("DELETE SurveyUnit {} ", surveyUnitId);
        surveyUnitService.deleteSurveyUnitById(surveyUnitId);
        Response response = new Response(String.format("SurveyUnit %s deleted", surveyUnitId), HttpStatus.OK);
        log.info("DELETE /survey-units/{surveyUnitId} resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Operation(summary = "Remove SurveyUnit from Sample", responses = {
        @ApiResponse(responseCode = "200", description = "SurveysUnit successfully added"),
        @ApiResponse(responseCode = "404", description = "Sample or SurveyUnit Not Found")
    })
    @DeleteMapping(path = "/{surveyUnitId}/samples/{sampleId}")
    public ResponseEntity<Object> removeSurveyUnitFromSample(
        @PathVariable("surveyUnitId") final Long surveyUnitId,
        @PathVariable("sampleId") final Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException {
        log.info("DELTE Remove SurveyUnit {} from Sample {}", surveyUnitId, sampleId);
        surveyUnitService.removeSurveyUnitFromSample(surveyUnitId, sampleId);
        Response response = new Response(String.format("SurveyUnit %s removed from sample %s", surveyUnitId, sampleId), HttpStatus.OK);
        log.info("DELETE /survey-units/{surveyUnitId}/sample/{sampleId} resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Operation(summary = "Get SurveyUnits by Sample", responses = {
        @ApiResponse(responseCode = "200", description = "SurveysUnits successfully recovered"),
        @ApiResponse(responseCode = "404", description = "Sample Not Found")
    })
    @GetMapping(path = "/samples/{sampleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SurveyUnitDto>> getSurveyUnitsBySample(@PathVariable("sampleId") final Long sampleId) throws SampleNotFoundException {
        log.info("Get SurveyUnits by Sample {}", sampleId);
        List<SurveyUnitDto> suList = surveyUnitService.getSurveyUnitsBySampleId(sampleId).stream().map(SampleSurveyUnitDto::getSurveyUnit).toList();
        return new ResponseEntity<>(suList, HttpStatus.OK);
    }

    @Operation(summary = "Get list of Survey Units Ids", responses = {
        @ApiResponse(responseCode = "200", description = "List of Survey Units Ids successfully recovered"),
        @ApiResponse(responseCode = "404", description = "Sample Not Found")
    })
    @GetMapping(path = "/samples/{sampleId}/ids")
    public ResponseEntity<List<Long>> getListOfIds(@PathVariable("sampleId") final Long sampleId) throws SampleNotFoundException {
        log.info("Get list of Survey Units Ids for sample {}", sampleId);
        return new ResponseEntity<>(surveyUnitService.getSurveyUnitIdsBySampleId(sampleId), HttpStatus.OK);
    }

    @Operation(summary = "Export identifiers mapping table", responses = {
        @ApiResponse(responseCode = "200", description = "Identifiers mapping table successfully recovered"),
        @ApiResponse(responseCode = "404", description = "Sample Not Found")
    })
    @GetMapping(path = "/samples/{sampleId}/ids-mapping-table", produces = "text/csv")
    public ResponseEntity<Object> getIdentifiersMappingTable(@PathVariable("sampleId") final Long sampleId) throws SampleNotFoundException, CsvFileException {
        log.info("Get identifiers mapping table for sample {}", sampleId);
        List<SuIdMappingRecord> records = surveyUnitService.getIdMappingTableBySampleId(sampleId);
        List<SuIdMappingCsvTarget> targets =
            records.stream().map(r -> SuIdMappingCsvTarget.builder().idRem(String.valueOf(r.repositoryId())).idSource(r.externalId()).build()).toList();
        ByteArrayInputStream csvStream = BeanToCsvUtils.write(targets);
        String fileName = "ids-mapping-table-sample-" + sampleId + ".csv";

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
            .contentType(MediaType.parseMediaType("text/csv")).body(new InputStreamResource(csvStream));
    }

}
