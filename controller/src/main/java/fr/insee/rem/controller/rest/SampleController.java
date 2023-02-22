package fr.insee.rem.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.insee.rem.controller.response.Response;
import fr.insee.rem.domain.dtos.SampleDto;
import fr.insee.rem.domain.exception.SampleAlreadyExistsException;
import fr.insee.rem.domain.exception.SampleNotFoundException;
import fr.insee.rem.domain.ports.api.SampleServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(path = "/sample")
public class SampleController {

    @Autowired
    SampleServicePort sampleService;

    @Operation(summary = "Delete sample", responses = {
        @ApiResponse(responseCode = "200", description = "Sample successfully deleted"), @ApiResponse(responseCode = "404", description = "Sample Not Found")
    })
    @DeleteMapping(path = "/{sampleId}")
    public ResponseEntity<Object> deleteSample(HttpServletRequest request, @PathVariable("sampleId") final Long sampleId) throws SampleNotFoundException {
        log.info("DELETE sample {}", sampleId);
        sampleService.deleteSampleById(sampleId);
        Response response = new Response(String.format("sample %s deleted", sampleId), HttpStatus.OK);
        log.info("DELETE /sample/{sampleId} resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
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
        return new ResponseEntity<>(sampleService.createSample(label), HttpStatus.OK);
    }

    @Operation(summary = "Get sample", responses = {
        @ApiResponse(responseCode = "200", description = "Sample successfully recovered"), @ApiResponse(responseCode = "404", description = "Sample Not Found")
    })
    @GetMapping(path = "/{sampleId}")
    public ResponseEntity<SampleDto> getSample(HttpServletRequest request, @PathVariable("sampleId") final Long sampleId) throws SampleNotFoundException {
        log.info("Get sample {}", sampleId);
        return new ResponseEntity<>(sampleService.getSampleById(sampleId), HttpStatus.OK);
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
