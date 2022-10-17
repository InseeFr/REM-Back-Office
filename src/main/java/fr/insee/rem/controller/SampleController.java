package fr.insee.rem.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.insee.rem.entities.Response;
import fr.insee.rem.entities.SurveyUnit;
import fr.insee.rem.service.SampleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(path = "/sample")
public class SampleController {

    @Autowired
    SampleService sampleService;

    @Operation(summary = "Add Sample from csv file")
    @PostMapping(path = "/{sampleId}/addFromCSVFile", consumes = {
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<Object> addSampleFromCSVFile(
        HttpServletRequest request,
        @PathVariable("sampleId") final Long sampleId,
        @RequestPart("sample") MultipartFile sampleFile) {
        log.info("POST add sample {} from csv file {}", sampleId, sampleFile.getOriginalFilename());
        Response response = sampleService.addSampleFromCSVFile(sampleId, sampleFile);
        log.info("POST /sample/{sampleId}/addFromCSVFile resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Operation(summary = "Delete sample")
    @DeleteMapping(path = "/{sampleId}/delete")
    public ResponseEntity<Object> deleteSample(HttpServletRequest request, @PathVariable("sampleId") final Long sampleId) {
        log.info("DELETE sample {}", sampleId);
        Response response = sampleService.deleteSample(sampleId);
        log.info("DELETE /sample/{sampleId}/delete resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }

    @Operation(summary = "Find sample")
    @GetMapping(path = "/{sampleId}/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SurveyUnit>> getSample(HttpServletRequest request, @PathVariable("sampleId") final Long sampleId) {
        log.info("GET sample {}", sampleId);
        return new ResponseEntity<>(sampleService.getSample(sampleId), HttpStatus.OK);
    }

    @Operation(summary = "Create sample")
    @PutMapping(path = "/create")
    public ResponseEntity<Object> putSample(HttpServletRequest request, @RequestBody String label) {
        log.info("PUT create sample {}", label);
        Response response = sampleService.putSample(label);
        log.info("PUT /sample/create resulting in {} with response [{}]", response.getHttpStatus(), response.getMessage());
        return new ResponseEntity<>(response.getMessage(), response.getHttpStatus());
    }
}
