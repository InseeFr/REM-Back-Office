package fr.insee.rem.controller.rest;

import fr.insee.rem.controller.exception.ApiError;
import fr.insee.rem.controller.response.PartitionWithCount;
import fr.insee.rem.controller.response.Response;
import fr.insee.rem.domain.dtos.PartitionDto;
import fr.insee.rem.domain.ports.api.PartitionServicePort;
import fr.insee.rem.domain.ports.api.SurveyUnitServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequestMapping(path = "/partitions")
public class PartitionController {

    @Autowired
    PartitionServicePort partitionService;

    @Autowired
    SurveyUnitServicePort surveyUnitService;

    @Tags(value = {
            @Tag(name = "3. Manage a partition"),
            @Tag(name = "1. Import data")
    })
    @Operation(summary = "Create partition", responses = {
            @ApiResponse(responseCode = "200", description = "Partition successfully created", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = PartitionWithCount.class))),
            @ApiResponse(responseCode = "409", description = "Partition Already Exists", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping(path = "/", consumes = {
            MediaType.TEXT_PLAIN_VALUE
    })
    public ResponseEntity<PartitionWithCount> createPartition(@RequestBody String label) {
        log.info("POST create partition {}", label);
        PartitionDto partition = partitionService.createPartition(label);
        return new ResponseEntity<>(PartitionWithCount.builder().partition(partition).count(0).build(), HttpStatus.OK);
    }

    @Tag(name = "2. Export data")
    @Operation(summary = "Get partition", responses = {
            @ApiResponse(responseCode = "200", description = "Partition successfully recovered", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = PartitionWithCount.class))),
            @ApiResponse(responseCode = "404", description = "Partition Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(path = "/{partitionId}")
    public ResponseEntity<PartitionWithCount> getPartition(@PathVariable("partitionId") final Long partitionId) {
        log.info("Get partition {}", partitionId);
        PartitionDto partition = partitionService.getPartitionById(partitionId);
        long count = surveyUnitService.countSurveyUnitsByPartition(partitionId);
        return new ResponseEntity<>(PartitionWithCount.builder().partition(partition).count(count).build(),
                HttpStatus.OK);
    }

    @Tag(name = "2. Export data")
    @Operation(summary = "Get all partitions", responses = {
            @ApiResponse(responseCode = "200", description = "Partitions successfully recovered", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = PartitionWithCount.class)))
    })
    @GetMapping(path = "/")
    public ResponseEntity<List<PartitionWithCount>> getAllPartitions() {
        log.info("Get all partitions");
        List<PartitionDto> partitionDtoList = partitionService.getAllPartitions();
        List<PartitionWithCount> partitionWithCountList =
                partitionDtoList.stream().map(dto -> PartitionWithCount.builder()
                                .partition(dto)
                                .count(surveyUnitService.countSurveyUnitsByPartition(dto.getPartitionId()))
                                .build())
                        .toList();
        return new ResponseEntity<>(partitionWithCountList, HttpStatus.OK);
    }

    @Tag(name = "5. Clean data")
    @Operation(summary = "Delete partition", responses = {
            @ApiResponse(responseCode = "200", description = "Partition successfully deleted", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Partition Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping(path = "/{partitionId}")
    public ResponseEntity<Response> deletePartition(@PathVariable("partitionId") final Long partitionId) {
        log.info("DELETE partition {}", partitionId);
        partitionService.deletePartitionById(partitionId);
        Response response = new Response(String.format("partition %s deleted", partitionId), HttpStatus.OK);
        log.info("DELETE /partitions/{partitionId} resulting in {} with response [{}]", response.getHttpStatus(),
                response.getMessage());
        return ResponseEntity.ok(response);
    }

    @Tag(name = "5. Clean data")
    @Operation(summary = "Empty a partition", responses = {
            @ApiResponse(responseCode = "200", description = "Partition successfully emptied", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = PartitionWithCount.class))),
            @ApiResponse(responseCode = "404", description = "Partition Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping(path = "/{partitionId}/empty")
    public ResponseEntity<PartitionWithCount> emptyPartition(@PathVariable("partitionId") final Long partitionId) {
        log.info("Empty partition {}", partitionId);
        partitionService.emptyPartitionById(partitionId);
        PartitionDto partition = partitionService.getPartitionById(partitionId);
        long count = surveyUnitService.countSurveyUnitsByPartition(partitionId);
        return new ResponseEntity<>(PartitionWithCount.builder().partition(partition).count(count).build(),
                HttpStatus.OK);
    }
}
