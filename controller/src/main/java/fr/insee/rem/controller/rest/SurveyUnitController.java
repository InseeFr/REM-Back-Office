package fr.insee.rem.controller.rest;

import fr.insee.rem.controller.adapter.HouseholdCsvAdapter;
import fr.insee.rem.controller.exception.CsvFileException;
import fr.insee.rem.controller.response.Response;
import fr.insee.rem.controller.sources.HouseholdCsvSource;
import fr.insee.rem.controller.targets.SuIdMappingCsvTarget;
import fr.insee.rem.controller.utils.BeanToCsvUtils;
import fr.insee.rem.controller.utils.CsvToBeanUtils;
import fr.insee.rem.domain.dtos.PartitionSurveyUnitLinkDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.exception.PartitionNotFoundException;
import fr.insee.rem.domain.exception.SettingsException;
import fr.insee.rem.domain.ports.api.SurveyUnitServicePort;
import fr.insee.rem.domain.records.SuIdMappingRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@Slf4j
@RequestMapping(path = "/survey-units")
public class SurveyUnitController {

    @Autowired
    SurveyUnitServicePort surveyUnitService;

    @Autowired
    HouseholdCsvAdapter householdCsvAdapter;

    @Tag(name = "Import new survey units")
    @Operation(summary = "Add Household SurveyUnits into existing partition from CSV file", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnits successfully added"),
            @ApiResponse(responseCode = "400", description = "CSV File Error"),
            @ApiResponse(responseCode = "404", description = "Partition Not Found")
    })
    @PostMapping(path = "/households/partitions/{partitionId}/csv-upload", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<Object> addHouseholdSurveyUnitsFromCSVFile(
            @PathVariable("partitionId") final Long partitionId,
            @RequestPart("partition") MultipartFile partitionFile) throws PartitionNotFoundException, CsvFileException {
        log.info("POST add partition {} from csv file {}", partitionId, partitionFile.getOriginalFilename());
        List<HouseholdCsvSource> householdCsvList = CsvToBeanUtils.parse(partitionFile, HouseholdCsvSource.class);
        List<SurveyUnitDto> surveyUnits = householdCsvList.stream().map(h -> householdCsvAdapter.convert(h))
                .toList();
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnitLinks =
                surveyUnitService.importSurveyUnitsIntoPartition(partitionId,
                        surveyUnits);
        Response response = new Response(String.format("%s surveyUnits created",
                importedPartitionSurveyUnitLinks.size()), HttpStatus.OK);
        log.info("POST /survey-units/partitions/{partitionId} resulting in {} with response [{}]", response
                .getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Tag(name = "Import new survey units")
    @Operation(summary = "Add Household SurveyUnits into existing partition from json (REM Model)", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnits successfully added"),
            @ApiResponse(responseCode = "404", description = "Partition Not Found")
    })
    @PostMapping(path = "/households/partitions/{partitionId}/json-upload")
    public ResponseEntity<Object> addHouseholdSurveyUnitsFromJson(
            @PathVariable("partitionId") final Long partitionId, @RequestBody List<SurveyUnitDto> surveyUnits) {
        log.info("POST add partition {} from json {}", partitionId);
        boolean hasRepositoryId = surveyUnitService.checkRepositoryId(surveyUnits);
        if (hasRepositoryId) {
            throw new SettingsException("Survey units already contain a repository identifier");
        }
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnitLinks =
                surveyUnitService.importSurveyUnitsIntoPartition(partitionId,
                        surveyUnits);
        Response response = new Response(String.format("%s surveyUnits created",
                importedPartitionSurveyUnitLinks.size()), HttpStatus.OK);
        log.info("POST /survey-units/partitions/{partitionId} resulting in {} with response [{}]", response
                .getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Tag(name = "Manage a partition")
    @Operation(summary = "Add SurveyUnit to Partition", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnit successfully added"),
            @ApiResponse(responseCode = "404", description = "Partition or SurveyUnit Not Found")
    })
    @PutMapping(path = "/{surveyUnitId}/partitions/{partitionId}")
    public ResponseEntity<Object> addSurveyUnitToPartition(
            @PathVariable("surveyUnitId") final Long surveyUnitId,
            @PathVariable("partitionId") final Long partitionId) {
        log.info("PUT Add SurveyUnit {} to Partition {}", surveyUnitId, partitionId);
        PartitionSurveyUnitLinkDto newPartitionSurveyUnitLink =
                surveyUnitService.addExistingSurveyUnitIntoPartition(surveyUnitId,
                        partitionId);
        Response response = new Response(String.format("SurveyUnit %s add to partition %s",
                newPartitionSurveyUnitLink.getSurveyUnit()
                        .getRepositoryId(), newPartitionSurveyUnitLink.getPartition().getPartitionId()), HttpStatus.OK);
        log.info("PUT /survey-units/{surveyUnitId}/partitions/{partitionId} resulting in {} with response [{}]",
                response
                        .getHttpStatus(), response.getMessage());

        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Tag(name = "Manage a partition")
    @Operation(summary = "Add SurveyUnits List to Partition", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnits successfully added"),
            @ApiResponse(responseCode = "404", description = "Partition or SurveyUnit Not Found")
    })
    @PutMapping(path = "/partitions/{partitionId}")
    public ResponseEntity<Object> addSurveyUnitsToPartition(@RequestBody List<Long> surveyUnitIds, @PathVariable(
            "partitionId") final Long partitionId) {
        log.info("PUT Add SurveyUnits List to Partition {}", partitionId);
        if (surveyUnitIds == null || surveyUnitIds.isEmpty()) {
            Response response = new Response("SurveyUnits List empty", HttpStatus.BAD_REQUEST);
            log.info("PUT /survey-units/partitions/{partitionId} resulting in {} with response [{}]", response
                    .getHttpStatus(), response.getMessage());
            return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
        }
        int count = surveyUnitService.addExistingSurveyUnitsToPartition(surveyUnitIds, partitionId);
        Response response = new Response(String
                .format("%s SurveyUnits add to partition %s", count, partitionId), HttpStatus.OK);
        log.info("PUT /survey-units/partitions/{partitionId} resulting in {} with response [{}]", response
                .getHttpStatus(), response.getMessage());

        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Tag(name = "Export data")
    @Operation(summary = "Get SurveyUnit by Id", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnit successfully recovered"),
            @ApiResponse(responseCode = "404", description = "SurveyUnit Not Found")
    })
    @GetMapping(path = "/{surveyUnitId}")
    public ResponseEntity<SurveyUnitDto> getSurveyUnit(@PathVariable("surveyUnitId") final Long surveyUnitId) {
        log.info("GET SurveyUnit {}", surveyUnitId);
        return new ResponseEntity<>(surveyUnitService.getSurveyUnitById(surveyUnitId), HttpStatus.OK);
    }

    @Tag(name = "Clean data")
    @Operation(summary = "Delete SurveyUnit", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnit successfully deleted"),
            @ApiResponse(responseCode = "404", description = "SurveyUnit Not Found")
    })
    @DeleteMapping(path = "/{surveyUnitId}")
    public ResponseEntity<Object> deleteSurveyUnit(@PathVariable("surveyUnitId") final Long surveyUnitId) {
        log.info("DELETE SurveyUnit {} ", surveyUnitId);
        surveyUnitService.deleteSurveyUnitById(surveyUnitId);
        Response response = new Response(String.format("SurveyUnit %s deleted", surveyUnitId), HttpStatus.OK);
        log.info("DELETE /survey-units/{surveyUnitId} resulting in {} with response [{}]", response
                .getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Tag(name = "Manage a partition")
    @Operation(summary = "Remove SurveyUnit from Partition", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnit successfully removed"),
            @ApiResponse(responseCode = "404", description = "Partition or SurveyUnit Not Found")
    })
    @DeleteMapping(path = "/{surveyUnitId}/partitions/{partitionId}")
    public ResponseEntity<Object> removeSurveyUnitFromPartition(@PathVariable("surveyUnitId") final Long surveyUnitId,
                                                                @PathVariable("partitionId") final Long partitionId) {
        log.info("DELETE Remove SurveyUnit {} from Partition {}", surveyUnitId, partitionId);
        surveyUnitService.removeSurveyUnitFromPartition(surveyUnitId, partitionId);
        Response response = new Response(String
                .format("SurveyUnit %s removed from partition %s", surveyUnitId, partitionId), HttpStatus.OK);
        log.info("DELETE /survey-units/{surveyUnitId}/partition/{partitionId} resulting in {} with response [{}]",
                response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Tag(name = "Export data")
    @Operation(summary = "Get SurveyUnits by Partition", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnits successfully recovered"),
            @ApiResponse(responseCode = "404", description = "Partition Not Found")
    })
    @GetMapping(path = "/partitions/{partitionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SurveyUnitDto>> getSurveyUnitsByPartition(@PathVariable("partitionId") final Long partitionId) {
        log.info("Get SurveyUnits by Partition {}", partitionId);
        List<SurveyUnitDto> suList = surveyUnitService.getSurveyUnitsByPartitionId(partitionId).stream()
                .map(PartitionSurveyUnitLinkDto::getSurveyUnit).sorted(Comparator.comparing(SurveyUnitDto::getRepositoryId))
                .toList();
        return new ResponseEntity<>(suList, HttpStatus.OK);
    }

    @Tag(name = "Export data")
    @Operation(summary = "Get list of Survey Units Ids", responses = {
            @ApiResponse(responseCode = "200", description = "List of Survey Units Ids successfully recovered"),
            @ApiResponse(responseCode = "404", description = "Partition Not Found")
    })
    @GetMapping(path = "/partitions/{partitionId}/ids")
    public ResponseEntity<List<Long>> getListOfIds(@PathVariable("partitionId") final Long partitionId) {
        log.info("Get list of Survey Units Ids for partition {}", partitionId);
        List<Long> ids = surveyUnitService.getSurveyUnitIdsByPartitionId(partitionId);
        Collections.sort(ids);
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @Tag(name = "Export data")
    @Operation(summary = "Export identifiers mapping table", responses = {
            @ApiResponse(responseCode = "200", description = "Identifiers mapping table successfully recovered"),
            @ApiResponse(responseCode = "404", description = "Partition Not Found")
    })
    @GetMapping(path = "/partitions/{partitionId}/ids-mapping-table", produces = "text/csv")
    public ResponseEntity<Object> getIdentifiersMappingTable(@PathVariable("partitionId") final Long partitionId) {
        log.info("Get identifiers mapping table for partition {}", partitionId);
        List<SuIdMappingRecord> surveyUnitIdsMappingRecords =
                surveyUnitService.getSurveyUnitIdsMappingTableByPartitionId(partitionId);
        List<SuIdMappingCsvTarget> surveyUnitIdsMappingCsvTargets =
                surveyUnitIdsMappingRecords.stream().map(r -> SuIdMappingCsvTarget.builder().idRem(String
                        .valueOf(r.repositoryId())).idSource(r.externalId()).build()).sorted(Comparator
                        .comparing(SuIdMappingCsvTarget::getIdRem)).toList();
        ByteArrayInputStream csvStream = BeanToCsvUtils.write(surveyUnitIdsMappingCsvTargets);
        String fileName = "ids-mapping-table-partition-" + partitionId + ".csv";

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType("text/csv")).body(new InputStreamResource(csvStream));
    }

    @Tag(name = "Update data")
    @Operation(summary = "Replace SurveyUnit Data", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnit successfully updated"),
            @ApiResponse(responseCode = "400", description = "Data error"),
            @ApiResponse(responseCode = "404", description = "SurveyUnit Not Found")
    })
    @PutMapping(path = "/update")
    public ResponseEntity<Object> replaceSurveyUnitData(@RequestBody SurveyUnitDto surveyUnit) {
        log.info("PUT Replace SurveyUnit data replaced");
        SurveyUnitDto updatedSurveyUnit = surveyUnitService.updateSurveyUnit(surveyUnit);
        Response response = new Response(String.format("SurveyUnit %s data replaced", updatedSurveyUnit
                .getRepositoryId()), HttpStatus.OK);
        log.info("PUT /survey-units/{surveyUnitId}/update resulting in {} with response [{}]", response
                .getHttpStatus(), response.getMessage());

        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

}
