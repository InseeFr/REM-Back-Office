package fr.insee.rem.controller.rest;

import fr.insee.rem.controller.exception.ApiError;
import fr.insee.rem.controller.response.Response;
import fr.insee.rem.domain.dtos.PartitionDto;
import fr.insee.rem.domain.ports.api.PartitionServicePort;
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

    @Tags(value = {
            @Tag(name = "3. Manage a partition"),
            @Tag(name = "1. Import data")
    })
    @Operation(summary = "Create partition", responses = {
            @ApiResponse(responseCode = "200", description = "Partition successfully created", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = PartitionDto.class))),
            @ApiResponse(responseCode = "409", description = "Partition Already Exists", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping(path = "/", consumes = {
            MediaType.TEXT_PLAIN_VALUE
    })
    public ResponseEntity<PartitionDto> createPartition(@RequestBody String label) {
        log.info("POST create partition {}", label);
        return new ResponseEntity<>(partitionService.createPartition(label), HttpStatus.OK);
    }

    @Tag(name = "2. Export data")
    @Operation(summary = "Get partition", responses = {
            @ApiResponse(responseCode = "200", description = "Partition successfully recovered", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = PartitionDto.class))),
            @ApiResponse(responseCode = "404", description = "Partition Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(path = "/{partitionId}")
    public ResponseEntity<PartitionDto> getPartition(@PathVariable("partitionId") final Long partitionId) {
        log.info("Get partition {}", partitionId);
        return new ResponseEntity<>(partitionService.getPartitionById(partitionId), HttpStatus.OK);
    }

    @Tag(name = "2. Export data")
    @Operation(summary = "Get all partitions", responses = {
            @ApiResponse(responseCode = "200", description = "Partitions successfully recovered", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = PartitionDto.class)))
    })
    @GetMapping(path = "/")
    public ResponseEntity<List<PartitionDto>> getAllPartitions() {
        log.info("Get all partitions");
        return new ResponseEntity<>(partitionService.getAllPartitions(), HttpStatus.OK);
    }

    @Tag(name = "5. Clean data")
    @Operation(summary = "Delete partition", responses = {
            @ApiResponse(responseCode = "200", description = "Partition successfully deleted", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Partition Not Found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping(path = "/{partitionId}")
    public ResponseEntity<Object> deletePartition(@PathVariable("partitionId") final Long partitionId) {
        log.info("DELETE partition {}", partitionId);
        partitionService.deletePartitionById(partitionId);
        Response response = new Response(String.format("partition %s deleted", partitionId), HttpStatus.OK);
        log.info("DELETE /partitions/{partitionId} resulting in {} with response [{}]", response.getHttpStatus(),
                response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }
}
