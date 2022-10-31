package fr.insee.rem.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import fr.insee.rem.dto.SampleDto;
import fr.insee.rem.dto.SurveyUnitDto;
import fr.insee.rem.entities.Response;
import fr.insee.rem.exception.CsvFileException;
import fr.insee.rem.exception.SampleNotFoundException;

public interface SampleService {

    Response addSampleFromCSVFile(Long sampleId, MultipartFile sampleFile) throws SampleNotFoundException, CsvFileException;

    Response deleteSample(Long sampleId) throws SampleNotFoundException;

    List<SurveyUnitDto> getSurveyUnitsBySample(Long sampleId);

    SampleDto putSample(String label);

    SampleDto getSample(Long sampleId) throws SampleNotFoundException;

    List<SampleDto> getAllSamples();

}
