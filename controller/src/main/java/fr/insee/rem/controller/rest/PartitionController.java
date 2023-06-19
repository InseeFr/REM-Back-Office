package fr.insee.rem.controller.rest;

import fr.insee.rem.controller.response.Response;
import fr.insee.rem.domain.dtos.PartitionDto;
import fr.insee.rem.domain.ports.api.PartitionServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Tag(name = "Manage a partition")
    @Operation(summary = "Create partition", responses = {
            @ApiResponse(responseCode = "200", description = "Partition successfully created"),
            @ApiResponse(responseCode = "409", description = "Partition Already Exists")
    })
    @PostMapping(path = "/", consumes = {
            MediaType.TEXT_PLAIN_VALUE
    })
    public ResponseEntity<PartitionDto> createPartition(@RequestBody String label) {
        log.info("POST create partition {}", label);
        return new ResponseEntity<>(partitionService.createPartition(label), HttpStatus.OK);
    }

    @Tag(name = "Export data")
    @Operation(summary = "Get partition", responses = {
            @ApiResponse(responseCode = "200", description = "Partition successfully recovered"),
            @ApiResponse(responseCode = "404", description = "Partition Not Found")
    })
    @GetMapping(path = "/{partitionId}")
    public ResponseEntity<PartitionDto> getPartition(@PathVariable("partitionId") final Long partitionId) {
        log.info("Get partition {}", partitionId);
        return new ResponseEntity<>(partitionService.getPartitionById(partitionId), HttpStatus.OK);
    }

    @Tag(name = "Export data")
    @Operation(summary = "Get all partitions", responses = {
            @ApiResponse(responseCode = "200", description = "Partitions successfully recovered")
    })
    @GetMapping(path = "/")
    public ResponseEntity<List<PartitionDto>> getAllPartitions() {
        log.info("Get all partitions");
        return new ResponseEntity<>(partitionService.getAllPartitions(), HttpStatus.OK);
    }

    @Tag(name = "Clean data")
    @Operation(summary = "Delete partition", responses = {
            @ApiResponse(responseCode = "200", description = "Partition successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Partition Not Found")
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
