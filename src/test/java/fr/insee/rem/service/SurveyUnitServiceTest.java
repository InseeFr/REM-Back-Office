package fr.insee.rem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import fr.insee.rem.entities.Response;
import fr.insee.rem.entities.Sample;
import fr.insee.rem.entities.SampleSurveyUnit;
import fr.insee.rem.entities.SampleSurveyUnitPK;
import fr.insee.rem.entities.SurveyUnit;
import fr.insee.rem.exception.SampleNotFoundException;
import fr.insee.rem.exception.SurveyUnitNotFoundException;
import fr.insee.rem.repository.SampleRepository;
import fr.insee.rem.repository.SampleSurveyUnitRepository;
import fr.insee.rem.repository.SurveyUnitRepository;
import fr.insee.rem.service.impl.SurveyUnitServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class SurveyUnitServiceTest {

    @InjectMocks
    SurveyUnitServiceImpl surveyUnitService;

    @Mock
    SampleRepository sampleRepository;

    @Mock
    SurveyUnitRepository surveyUnitRepository;

    @Mock
    SampleSurveyUnitRepository sampleSurveyUnitRepository;

    @Test
    void addSurveyUnitToSample_success() throws Exception {
        Sample sample = new Sample();
        sample.setId(1l);
        SurveyUnit su = new SurveyUnit();
        su.setId(1l);
        SampleSurveyUnit ssu = new SampleSurveyUnit(sample, su);
        when(sampleRepository.findById(1l)).thenReturn(Optional.of(sample));
        when(surveyUnitRepository.findById(1l)).thenReturn(Optional.of(su));
        when(sampleSurveyUnitRepository.save(ssu)).thenReturn(ssu);
        Response response = surveyUnitService.addSurveyUnitToSample(1l, 1l);
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addSurveyUnitToSample_error() throws Exception {
        when(sampleRepository.findById(1l)).thenReturn(Optional.empty());
        Exception ex = Assertions.assertThrows(SampleNotFoundException.class, () -> surveyUnitService.addSurveyUnitToSample(1l, 1l));
        assertThat(ex.getMessage()).isEqualTo(String.format("Sample [%s] doesn't exist", 1));
        Sample sample = new Sample();
        sample.setId(1l);
        when(sampleRepository.findById(1l)).thenReturn(Optional.of(sample));
        when(surveyUnitRepository.findById(1l)).thenReturn(Optional.empty());
        Exception ex2 = Assertions.assertThrows(SurveyUnitNotFoundException.class, () -> surveyUnitService.addSurveyUnitToSample(1l, 1l));
        assertThat(ex2.getMessage()).isEqualTo(String.format("SurveyUnit [%s] doesn't exist", 1));
    }

    @Test
    void deleteSurveyUnit_success() throws Exception {
        SurveyUnit su = new SurveyUnit();
        su.setId(1l);
        when(surveyUnitRepository.findById(1l)).thenReturn(Optional.of(su));
        doNothing().when(sampleSurveyUnitRepository).deleteAll(Mockito.anyList());
        doNothing().when(surveyUnitRepository).delete(su);
        Response response = surveyUnitService.deleteSurveyUnit(1l);
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteSurveyUnit_error_404() throws Exception {
        when(surveyUnitRepository.findById(1l)).thenReturn(Optional.empty());
        Assertions.assertThrows(SurveyUnitNotFoundException.class, () -> surveyUnitService.deleteSurveyUnit(1l));
    }

    @Test
    void removeSurveyUnitFromSample_success() throws Exception {
        Sample sample = new Sample();
        sample.setId(1l);
        SurveyUnit su = new SurveyUnit();
        su.setId(1l);
        when(sampleRepository.findById(1l)).thenReturn(Optional.of(sample));
        when(surveyUnitRepository.findById(1l)).thenReturn(Optional.of(su));
        SampleSurveyUnitPK ssuPK = new SampleSurveyUnitPK(1l, 1l);
        doNothing().when(sampleSurveyUnitRepository).deleteById(ssuPK);
        Response response = surveyUnitService.removeSurveyUnitFromSample(1l, 1l);
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void removeSurveyUnitFromSample_error_404() throws Exception {
        when(sampleRepository.findById(1l)).thenReturn(Optional.empty());
        Assertions.assertThrows(SampleNotFoundException.class, () -> surveyUnitService.removeSurveyUnitFromSample(1l, 1l));
        Sample sample = new Sample();
        sample.setId(1l);
        when(sampleRepository.findById(1l)).thenReturn(Optional.of(sample));
        when(surveyUnitRepository.findById(1l)).thenReturn(Optional.empty());
        Assertions.assertThrows(SurveyUnitNotFoundException.class, () -> surveyUnitService.removeSurveyUnitFromSample(1l, 1l));
    }
}
