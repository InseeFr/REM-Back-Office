package fr.insee.rem.domain.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import fr.insee.rem.domain.dtos.SampleDto;
import fr.insee.rem.domain.exception.SampleAlreadyExistsException;
import fr.insee.rem.domain.exception.SampleNotFoundException;
import fr.insee.rem.domain.ports.spi.SamplePersistencePort;

@ExtendWith(MockitoExtension.class)
class SampleServiceTest {

    @InjectMocks
    SampleServiceImpl sampleService;

    @Mock
    SamplePersistencePort samplePersistencePort;

    @Test
    void createSample_does_not_throw() {

        String newSample = "sample1";

        when(samplePersistencePort.existsByLabel(newSample)).thenReturn(false);

        when(samplePersistencePort.createSample(Mockito.anyString())).then(new Answer<SampleDto>() {
            long sequence = 1l;

            @Override
            public SampleDto answer(InvocationOnMock invocation) throws Throwable {
                String label = (String) invocation.getArgument(0);
                SampleDto sampleDto = SampleDto.builder().id(sequence ++ ).label(label).build();
                return sampleDto;
            }
        });

        try {
            SampleDto insertedSample = sampleService.createSample(newSample);
            verify(samplePersistencePort).existsByLabel(newSample);
            verify(samplePersistencePort).createSample(Mockito.anyString());
            Assertions.assertNotNull(insertedSample.getId());
            Assertions.assertEquals(newSample, insertedSample.getLabel());
        }
        catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }

    }

    @Test
    void createSample_throw() {
        when(samplePersistencePort.existsByLabel(Mockito.anyString())).thenReturn(true);
        Assertions.assertThrows(SampleAlreadyExistsException.class, () -> sampleService.createSample(Mockito.anyString()));
    }

    @Test
    void getSampleById_does_not_throw() {

        SampleDto sampleDto = SampleDto.builder().id(1l).label("sample1").build();

        when(samplePersistencePort.findById(1l)).thenReturn(Optional.of(sampleDto));

        try {
            SampleDto sample = sampleService.getSampleById(1l);
            verify(samplePersistencePort).findById(1l);
            Assertions.assertNotNull(sample.getLabel());
        }
        catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }

    }

    @Test
    void getSampleById_throw() {
        when(samplePersistencePort.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(SampleNotFoundException.class, () -> sampleService.getSampleById(Mockito.anyLong()));
    }

    @Test
    void getAllSamples() {

        SampleDto sample1 = SampleDto.builder().id(1l).label("sample1").build();
        SampleDto sample2 = SampleDto.builder().id(2l).label("sample2").build();
        List<SampleDto> samples = List.of(sample1, sample2);

        when(samplePersistencePort.findAll()).thenReturn(samples);

        List<SampleDto> returnList = sampleService.getAllSamples();
        Assertions.assertTrue( !returnList.isEmpty());
        Assertions.assertEquals(2, returnList.size());
        Assertions.assertEquals(sample1, returnList.get(0));
        Assertions.assertEquals(sample2, returnList.get(1));

    }

    @Test
    void deleteSampleById_does_not_throw() {

        when(samplePersistencePort.existsById(1l)).thenReturn(true);
        doNothing().when(samplePersistencePort).deleteById(1l);
        try {
            sampleService.deleteSampleById(1l);
            verify(samplePersistencePort).existsById(1l);
            verify(samplePersistencePort).deleteById(1l);
        }
        catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }

    }

    @Test
    void deleteSampleById_throw() {
        when(samplePersistencePort.existsById(1l)).thenReturn(false);
        Assertions.assertThrows(SampleNotFoundException.class, () -> sampleService.deleteSampleById(1l));
    }
}
