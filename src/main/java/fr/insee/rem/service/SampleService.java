package fr.insee.rem.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import fr.insee.rem.entities.Response;
import fr.insee.rem.entities.SurveyUnit;

public interface SampleService {

    Response addSampleFromCSVFile(Long sampleId, MultipartFile sampleFile);

    Response deleteSample(Long sampleId);

    List<SurveyUnit> getSample(Long sampleId);

    Response putSample(String label);

}
