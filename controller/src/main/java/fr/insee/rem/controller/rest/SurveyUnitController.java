package fr.insee.rem.controller.rest;

import com.fasterxml.jackson.annotation.JsonView;
import fr.insee.rem.controller.adapter.CensusAdapter;
import fr.insee.rem.controller.adapter.HouseholdCsvAdapter;
import fr.insee.rem.controller.exception.ApiError;
import fr.insee.rem.controller.exception.CsvFileException;
import fr.insee.rem.controller.response.Response;
import fr.insee.rem.controller.response.SuIdMappingCsv;
import fr.insee.rem.controller.response.SuIdMappingJson;
import fr.insee.rem.controller.sources.CensusSource;
import fr.insee.rem.controller.sources.HouseholdCsvSource;
import fr.insee.rem.controller.utils.BeanToCsvUtils;
import fr.insee.rem.controller.utils.CsvToBeanUtils;
import fr.insee.rem.domain.dtos.PartitionSurveyUnitLinkDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.exception.PartitionNotFoundException;
import fr.insee.rem.domain.exception.SettingsException;
import fr.insee.rem.domain.ports.api.SurveyUnitServicePort;
import fr.insee.rem.domain.records.SuIdMappingRecord;
import fr.insee.rem.domain.views.Views;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
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

    @Autowired
    CensusAdapter censusAdapter;

    @Tag(name = "1. Import data")
    @Operation(summary = "Add Household SurveyUnits into existing partition from CSV file", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnits successfully added", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = SuIdMappingJson.class))),
            @ApiResponse(responseCode = "400", description = "CSV File Error", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Partition Not Found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    @PostMapping(path = "/households/partitions/{partitionId}/csv-upload", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<SuIdMappingJson> addHouseholdSurveyUnitsFromCSVFile(
            @PathVariable("partitionId") final Long partitionId,
            @RequestPart("partition") MultipartFile partitionFile) throws PartitionNotFoundException, CsvFileException {
        log.info("POST add partition {} from csv file {}", partitionId, partitionFile.getOriginalFilename());
        List<HouseholdCsvSource> householdCsvList = CsvToBeanUtils.parse(partitionFile, HouseholdCsvSource.class);
        List<SurveyUnitDto> surveyUnits = householdCsvList.stream().map(h -> householdCsvAdapter.convert(h)).toList();
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnitLinks =
                surveyUnitService.importSurveyUnitsIntoPartition(partitionId, surveyUnits);
        SuIdMappingJson suIdMappingJson = SuIdMappingJson.builder()
                .partitionId(partitionId)
                .data(importedPartitionSurveyUnitLinks.stream()
                        .map(p -> new SuIdMappingRecord(p.getSurveyUnit().getRepositoryId(),
                                p.getSurveyUnit().getExternalId()))
                        .toList())
                .count(importedPartitionSurveyUnitLinks.size())
                .build();
        return new ResponseEntity<>(suIdMappingJson, HttpStatus.OK);
    }

    @Tag(name = "1. Import data")
    @Operation(summary = "Add Household SurveyUnits into existing partition from json (REM Model)", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnits successfully added", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = SuIdMappingJson.class))),
            @ApiResponse(responseCode = "404", description = "Partition Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping(path = "/households/partitions/{partitionId}/json-upload")
    public ResponseEntity<SuIdMappingJson> addHouseholdSurveyUnitsFromJson(
            @PathVariable("partitionId") final Long partitionId,
            @RequestBody @JsonView(Views.SurveyUnitWithExternals.class) List<SurveyUnitDto> surveyUnits) {
        log.info("POST add partition {} from json", partitionId);
        boolean hasRepositoryId = surveyUnitService.checkRepositoryId(surveyUnits);
        if (hasRepositoryId) {
            throw new SettingsException("Survey units already contain a repository identifier");
        }
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnitLinks =
                surveyUnitService.importSurveyUnitsIntoPartition(partitionId, surveyUnits);

        SuIdMappingJson suIdMappingJson = SuIdMappingJson.builder()
                .partitionId(partitionId)
                .data(importedPartitionSurveyUnitLinks.stream()
                        .map(p -> new SuIdMappingRecord(p.getSurveyUnit().getRepositoryId(),
                                p.getSurveyUnit().getExternalId()))
                        .toList())
                .count(importedPartitionSurveyUnitLinks.size())
                .build();
        return new ResponseEntity<>(suIdMappingJson, HttpStatus.OK);
    }

    @Tag(name = "1. Import data")
    @Operation(summary = "Add Census SurveyUnits into existing partition", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnits successfully added", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = SuIdMappingJson.class))),
            @ApiResponse(responseCode = "404", description = "Partition Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping(path = "/households/partitions/{partitionId}/census-upload")
    public ResponseEntity<SuIdMappingJson> addCensusSurveyUnits(
            @PathVariable("partitionId") final Long partitionId,
            @RequestBody @JsonView(Views.SurveyUnitWithExternals.class) List<CensusSource> censusSurveyUnits) {
        log.info("POST add partition {} from json", partitionId);

        List<SurveyUnitDto> surveyUnits = censusSurveyUnits.stream().map(c -> censusAdapter.convert(c)).toList();
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnitLinks =
                surveyUnitService.importSurveyUnitsIntoPartition(partitionId, surveyUnits);

        SuIdMappingJson suIdMappingJson = SuIdMappingJson.builder()
                .partitionId(partitionId)
                .data(importedPartitionSurveyUnitLinks.stream()
                        .map(p -> new SuIdMappingRecord(p.getSurveyUnit().getRepositoryId(),
                                p.getSurveyUnit().getExternalId()))
                        .toList())
                .count(importedPartitionSurveyUnitLinks.size())
                .build();
        return new ResponseEntity<>(suIdMappingJson, HttpStatus.OK);
    }

    @Tag(name = "3. Manage a partition")
    @Operation(summary = "Add SurveyUnit to Partition", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnit successfully added", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Partition or SurveyUnit Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping(path = "/{surveyUnitId}/partitions/{partitionId}")
    public ResponseEntity<Response> addSurveyUnitToPartition(
            @PathVariable("surveyUnitId") final Long surveyUnitId,
            @PathVariable("partitionId") final Long partitionId) {
        log.info("PUT Add SurveyUnit {} to Partition {}", surveyUnitId, partitionId);
        PartitionSurveyUnitLinkDto newPartitionSurveyUnitLink =
                surveyUnitService.addExistingSurveyUnitIntoPartition(surveyUnitId, partitionId);
        Response response = new Response(String.format("SurveyUnit %s add to partition %s",
                newPartitionSurveyUnitLink.getSurveyUnit().getRepositoryId(),
                newPartitionSurveyUnitLink.getPartition().getPartitionId()), HttpStatus.OK);
        log.info("PUT /survey-units/{surveyUnitId}/partitions/{partitionId} resulting in {} with response [{}]",
                response.getHttpStatus(), response.getMessage());

        return ResponseEntity.ok(response);
    }

    @Tag(name = "3. Manage a partition")
    @Operation(summary = "Add SurveyUnits List to Partition", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnits successfully added", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Partition or SurveyUnit Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping(path = "/partitions/{partitionId}")
    public ResponseEntity<Response> addSurveyUnitsToPartition(@RequestBody List<Long> surveyUnitIds, @PathVariable(
            "partitionId") final Long partitionId) {
        log.info("PUT Add SurveyUnits List to Partition {}", partitionId);
        if (surveyUnitIds == null || surveyUnitIds.isEmpty()) {
            Response response = new Response("SurveyUnits List empty", HttpStatus.BAD_REQUEST);
            log.info("PUT /survey-units/partitions/{partitionId} resulting in {} with response [{}]",
                    response.getHttpStatus(), response.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        int count = surveyUnitService.addExistingSurveyUnitsToPartition(surveyUnitIds, partitionId);
        Response response = new Response(String.format("%s SurveyUnits add to partition %s", count, partitionId)
                , HttpStatus.OK);
        log.info("PUT /survey-units/partitions/{partitionId} resulting in {} with response [{}]", response
                .getHttpStatus(), response.getMessage());

        return ResponseEntity.ok(response);
    }

    @Tag(name = "2. Export data")
    @Operation(summary = "Get SurveyUnit by Id", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnit successfully recovered", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = SurveyUnitDto.class))),
            @ApiResponse(responseCode = "404", description = "SurveyUnit Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(path = "/{surveyUnitId}")
    public ResponseEntity<MappingJacksonValue> getSurveyUnit(
            @PathVariable("surveyUnitId") final Long surveyUnitId,
            @RequestParam(name = "withExternals", defaultValue = "false", required = false) final boolean withExternals) {
        log.info("GET SurveyUnit {}, withExternals: {}", surveyUnitId, withExternals);
        SurveyUnitDto surveyUnit = surveyUnitService.getSurveyUnitById(surveyUnitId);
        MappingJacksonValue result = new MappingJacksonValue(surveyUnit);
        result.setSerializationView(Views.SurveyUnitWithId.class);
        if (withExternals) {
            result.setSerializationView(Views.SurveyUnitWithIdAndExternals.class);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Tag(name = "5. Clean data")
    @Operation(summary = "Delete SurveyUnit", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnit successfully deleted", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "SurveyUnit Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping(path = "/{surveyUnitId}")
    public ResponseEntity<Response> deleteSurveyUnit(@PathVariable("surveyUnitId") final Long surveyUnitId) {
        log.info("DELETE SurveyUnit {} ", surveyUnitId);
        surveyUnitService.deleteSurveyUnitById(surveyUnitId);
        Response response = new Response(String.format("SurveyUnit %s deleted", surveyUnitId), HttpStatus.OK);
        log.info("DELETE /survey-units/{surveyUnitId} resulting in {} with response [{}]", response
                .getHttpStatus(), response.getMessage());
        return ResponseEntity.ok(response);
    }

    @Tag(name = "3. Manage a partition")
    @Operation(summary = "Remove SurveyUnit from Partition", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnit successfully removed", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Partition or SurveyUnit Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping(path = "/{surveyUnitId}/partitions/{partitionId}")
    public ResponseEntity<Response> removeSurveyUnitFromPartition(
            @PathVariable("surveyUnitId") final Long surveyUnitId,
            @PathVariable("partitionId") final Long partitionId) {
        log.info("DELETE Remove SurveyUnit {} from Partition {}", surveyUnitId, partitionId);
        surveyUnitService.removeSurveyUnitFromPartition(surveyUnitId, partitionId);
        Response response = new Response(String
                .format("SurveyUnit %s removed from partition %s", surveyUnitId, partitionId), HttpStatus.OK);
        log.info("DELETE /survey-units/{surveyUnitId}/partition/{partitionId} resulting in {} with response [{}]",
                response.getHttpStatus(), response.getMessage());
        return ResponseEntity.ok(response);
    }

    @Tag(name = "2. Export data")
    @Operation(summary = "Get SurveyUnits by Partition", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnits successfully recovered", content =
            @Content(mediaType = "application/json", array =
            @ArraySchema(schema = @Schema(implementation = SurveyUnitDto.class)))),
            @ApiResponse(responseCode = "404", description = "Partition Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(path = "/partitions/{partitionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MappingJacksonValue> getSurveyUnitsByPartition(
            @PathVariable("partitionId") final Long partitionId, @RequestParam(name = "withExternals",
            defaultValue = "false", required = false) final boolean withExternals) {
        log.info("Get SurveyUnits by Partition {}", partitionId);
        List<SurveyUnitDto> suList = surveyUnitService.getSurveyUnitsByPartitionId(partitionId).stream()
                .map(PartitionSurveyUnitLinkDto::getSurveyUnit)
                .sorted(Comparator.comparing(SurveyUnitDto::getRepositoryId))
                .toList();
        MappingJacksonValue result = new MappingJacksonValue(suList);
        result.setSerializationView(Views.SurveyUnitWithId.class);
        if (withExternals) {
            result.setSerializationView(Views.SurveyUnitWithIdAndExternals.class);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Tag(name = "2. Export data")
    @Operation(summary = "Get list of Survey Units Ids", responses = {
            @ApiResponse(responseCode = "200", description = "Survey Units Ids successfully recovered", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(type = "integer")))),
            @ApiResponse(responseCode = "404", description = "Partition Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(path = "/partitions/{partitionId}/ids")
    public ResponseEntity<List<Long>> getListOfIds(@PathVariable("partitionId") final Long partitionId) {
        log.info("Get list of Survey Units Ids for partition {}", partitionId);
        List<Long> ids = surveyUnitService.getSurveyUnitIdsByPartitionId(partitionId);
        Collections.sort(ids);
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @Tag(name = "2. Export data")
    @Operation(summary = "Export identifiers mapping table", responses = {
            @ApiResponse(responseCode = "200", description = "Identifiers mapping table successfully recovered",
                    content = @Content(mediaType = "text/csv")),
            @ApiResponse(responseCode = "404", description = "Partition Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(path = "/partitions/{partitionId}/ids-mapping-table", produces = "text/csv")
    public ResponseEntity<Object> getIdentifiersMappingTable(@PathVariable("partitionId") final Long partitionId) {
        log.info("Get identifiers mapping table for partition {}", partitionId);
        List<SuIdMappingRecord> surveyUnitIdsMappingRecords =
                surveyUnitService.getSurveyUnitIdsMappingTableByPartitionId(partitionId);
        List<SuIdMappingCsv> surveyUnitIdsMappingCsvTargets =
                surveyUnitIdsMappingRecords.stream().map(r -> SuIdMappingCsv.builder().idRem(String
                        .valueOf(r.repositoryId())).idSource(r.externalId()).build()).sorted(Comparator
                        .comparing(SuIdMappingCsv::getIdRem)).toList();
        ByteArrayInputStream csvStream = BeanToCsvUtils.write(surveyUnitIdsMappingCsvTargets);
        String fileName = "ids-mapping-table-partition-" + partitionId + ".csv";

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType("text/csv")).body(new InputStreamResource(csvStream));
    }

    @Tag(name = "4. Update data")
    @Operation(summary = "Replace SurveyUnit Data", responses = {
            @ApiResponse(responseCode = "200", description = "SurveyUnit successfully updated", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Data error", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "SurveyUnit Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping(path = "/update")
    public ResponseEntity<Response> replaceSurveyUnitData(@RequestBody @JsonView(Views.SurveyUnitWithIdAndExternals.class) SurveyUnitDto surveyUnit) {
        log.info("PUT Replace SurveyUnit data replaced");
        SurveyUnitDto updatedSurveyUnit = surveyUnitService.updateSurveyUnit(surveyUnit);
        Response response = new Response(String.format("SurveyUnit %s data replaced", updatedSurveyUnit
                .getRepositoryId()), HttpStatus.OK);
        log.info("PUT /survey-units/{surveyUnitId}/update resulting in {} with response [{}]", response
                .getHttpStatus(), response.getMessage());

        return ResponseEntity.ok(response);
    }

}
