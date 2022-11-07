package fr.insee.rem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import fr.insee.rem.entities.Response;
import fr.insee.rem.entities.Sample;
import fr.insee.rem.exception.CsvFileException;
import fr.insee.rem.exception.SampleAlreadyExistsException;
import fr.insee.rem.repository.SampleRepository;
import fr.insee.rem.repository.SampleSurveyUnitRepository;
import fr.insee.rem.repository.SurveyUnitRepository;
import fr.insee.rem.service.impl.SampleServiceImpl;

@ExtendWith(MockitoExtension.class)
class SampleServiceTest {

    @InjectMocks
    SampleServiceImpl sampleService;

    @Mock
    SampleRepository sampleRepository;

    @Mock
    SurveyUnitRepository surveyUnitRepository;

    @Mock
    SampleSurveyUnitRepository sampleSurveyUnitRepository;

    @Mock
    private EntityManager entityManager;

    String sample1 = "test_sample1.csv";
    String sample1Error = "test_sample1_error.csv";

    @Test
    void addSampleFromCSVFile_success() throws Exception {
        File file = new File(getClass().getClassLoader().getResource(sample1).getFile());
        MultipartFile sampleFile = new MockMultipartFile("sample", file.getName(), MediaType.TEXT_PLAIN_VALUE, new FileInputStream(file));
        when(surveyUnitRepository.saveAll(Mockito.anyList())).thenReturn(new ArrayList<>());
        doNothing().when(entityManager).persist(Mockito.any());
        Sample sample = new Sample();
        sample.setId(1l);
        when(sampleRepository.findById(1l)).thenReturn(Optional.of(sample));
        Response response = sampleService.addSampleFromCSVFile(1l, sampleFile);
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addSampleFromCSVFile_error() throws Exception {
        File file = new File(getClass().getClassLoader().getResource(sample1Error).getFile());
        MultipartFile sampleFile = new MockMultipartFile("sample", file.getName(), MediaType.TEXT_PLAIN_VALUE, new FileInputStream(file));
        Sample sample = new Sample();
        sample.setId(1l);
        when(sampleRepository.findById(1l)).thenReturn(Optional.of(sample));
        Assertions.assertThrows(CsvFileException.class, () -> sampleService.addSampleFromCSVFile(1l, sampleFile));
    }

    @Test
    void putSample_error() throws Exception {
        Sample sample = new Sample();
        sample.setId(1l);
        when(sampleRepository.findByLabel(Mockito.anyString())).thenReturn(Optional.of(sample));
        Assertions.assertThrows(SampleAlreadyExistsException.class, () -> sampleService.putSample(Mockito.anyString()));
    }

}
