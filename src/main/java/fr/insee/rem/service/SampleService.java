package fr.insee.rem.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import fr.insee.rem.entities.Response;
import fr.insee.rem.entities.Sample;
import fr.insee.rem.entities.SurveyUnit;
import fr.insee.rem.exception.CsvFileException;
import fr.insee.rem.exception.SampleNotFoundException;

public interface SampleService {

    Response addSampleFromCSVFile(Long sampleId, MultipartFile sampleFile) throws SampleNotFoundException, CsvFileException;

    Response deleteSample(Long sampleId) throws SampleNotFoundException;

    List<SurveyUnit> getSurveyUnitsBySample(Long sampleId);

    Response putSample(String label);

    Sample getSample(Long sampleId) throws SampleNotFoundException;

    List<Sample> getAllSamples();

}
