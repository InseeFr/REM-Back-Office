package fr.insee.rem.domain.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import fr.insee.rem.domain.dtos.SampleSurveyUnitDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.dtos.TypeUnit;
import fr.insee.rem.domain.exception.SampleNotFoundException;
import fr.insee.rem.domain.exception.SurveyUnitNotFoundException;
import fr.insee.rem.domain.ports.spi.SamplePersistencePort;
import fr.insee.rem.domain.ports.spi.SampleSurveyUnitPersistencePort;
import fr.insee.rem.domain.ports.spi.SurveyUnitPersistencePort;

@ExtendWith(MockitoExtension.class)
class SurveyUnitServiceTest {

    @InjectMocks
    SurveyUnitServiceImpl surveyUnitService;

    @Mock
    SampleSurveyUnitPersistencePort sampleSurveyUnitPersistencePort;

    @Mock
    SurveyUnitPersistencePort surveyUnitPersistencePort;

    @Mock
    SamplePersistencePort samplePersistencePort;

    @Test
    void importSurveyUnitsToSample_does_not_throw() {

        SurveyUnitDto su1 = SurveyUnitDto.builder().typeUnit(TypeUnit.HOUSEHOLD).build();
        SurveyUnitDto su2 = SurveyUnitDto.builder().typeUnit(TypeUnit.HOUSEHOLD).build();

        List<SurveyUnitDto> suList = List.of(su1, su2);

        when(samplePersistencePort.existsById(1l)).thenReturn(true);

        when(sampleSurveyUnitPersistencePort.saveAll(1l, suList)).then(new Answer<List<SampleSurveyUnitDto>>() {
            long sequence = 1l;

            SampleDto sample1 = SampleDto.builder().id(1l).label("sample1").build();

            @Override
            public List<SampleSurveyUnitDto> answer(InvocationOnMock invocation) throws Throwable {
                List<SurveyUnitDto> suList = invocation.getArgument(1);
                List<SampleSurveyUnitDto> ssuList = new ArrayList<>();
                suList.stream().forEach(su -> {
                    su.setRepositoryId(sequence ++ );
                    SampleSurveyUnitDto ssu = SampleSurveyUnitDto.builder().sample(sample1).surveyUnit(su).build();
                    ssuList.add(ssu);
                });

                return ssuList;
            }
        });

        try {
            List<SampleSurveyUnitDto> insertedListSsu = surveyUnitService.importSurveyUnitsToSample(1l, suList);
            verify(samplePersistencePort).existsById(1l);
            verify(sampleSurveyUnitPersistencePort).saveAll(1l, suList);
            Assertions.assertNotNull(insertedListSsu.get(0).getSurveyUnit().getRepositoryId());
            Assertions.assertEquals(1l, insertedListSsu.get(0).getSurveyUnit().getRepositoryId());
            Assertions.assertEquals(2l, insertedListSsu.get(1).getSurveyUnit().getRepositoryId());
            Assertions.assertEquals(1l, insertedListSsu.get(0).getSample().getId());
        }
        catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }
    }

    @Test
    void importSurveyUnitsToSample_throw() {
        when(samplePersistencePort.existsById(Mockito.anyLong())).thenReturn(false);
        Assertions.assertThrows(SampleNotFoundException.class,
            () -> surveyUnitService.importSurveyUnitsToSample(Mockito.anyLong(), new ArrayList<SurveyUnitDto>()));
    }

    @Test
    void addSurveyUnitToSample_does_not_throw() {

        Optional<SampleDto> sampleDto = Optional.of(SampleDto.builder().id(1l).label("Does Not Throw").build());
        when(samplePersistencePort.existsById(1l)).thenReturn(true);
        Optional<SurveyUnitDto> suDto = Optional.of(SurveyUnitDto.builder().repositoryId(1l).build());
        when(surveyUnitPersistencePort.existsById(1l)).thenReturn(true);
        SampleSurveyUnitDto ssuDto = SampleSurveyUnitDto.builder().sample(sampleDto.get()).surveyUnit(suDto.get()).build();
        when(sampleSurveyUnitPersistencePort.addSurveyUnitToSample(1l, 1l)).thenReturn(ssuDto);

        try {
            SampleSurveyUnitDto insertedSsu = surveyUnitService.addSurveyUnitToSample(1l, 1l);
            verify(samplePersistencePort).existsById(1l);
            verify(surveyUnitPersistencePort).existsById(1l);
            verify(sampleSurveyUnitPersistencePort).addSurveyUnitToSample(1l, 1l);
            Assertions.assertEquals(1l, insertedSsu.getSurveyUnit().getRepositoryId());
            Assertions.assertEquals(1l, insertedSsu.getSample().getId());
        }
        catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }
    }

    @Test
    void addSurveyUnitToSample_throw() {
        when(samplePersistencePort.existsById(Mockito.anyLong())).thenReturn(false);
        Assertions.assertThrows(SampleNotFoundException.class, () -> surveyUnitService.addSurveyUnitToSample(1l, Mockito.anyLong()));
        when(samplePersistencePort.existsById(1l)).thenReturn(true);
        when(surveyUnitPersistencePort.existsById(Mockito.anyLong())).thenReturn(false);
        Assertions.assertThrows(SurveyUnitNotFoundException.class, () -> surveyUnitService.addSurveyUnitToSample(Mockito.anyLong(), 1l));
    }

    @Test
    void deleteSurveyUnitById_does_not_throw() {

        when(surveyUnitPersistencePort.existsById(1l)).thenReturn(true);

        doNothing().when(surveyUnitPersistencePort).deleteById(1l);

        try {
            surveyUnitService.deleteSurveyUnitById(1l);
            verify(surveyUnitPersistencePort).existsById(1l);
            verify(surveyUnitPersistencePort).deleteById(1l);
        }
        catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }
    }

    @Test
    void deleteSurveyUnitById_throw() {
        when(surveyUnitPersistencePort.existsById(Mockito.anyLong())).thenReturn(false);
        Assertions.assertThrows(SurveyUnitNotFoundException.class, () -> surveyUnitService.deleteSurveyUnitById(Mockito.anyLong()));
    }

    @Test
    void removeSurveyUnitFromSample_does_not_throw() {

        when(samplePersistencePort.existsById(1l)).thenReturn(true);
        when(surveyUnitPersistencePort.existsById(1l)).thenReturn(true);

        doNothing().when(sampleSurveyUnitPersistencePort).removeSurveyUnitFromSample(1l, 1l);

        try {
            surveyUnitService.removeSurveyUnitFromSample(1l, 1l);
            verify(samplePersistencePort).existsById(1l);
            verify(surveyUnitPersistencePort).existsById(1l);
            verify(sampleSurveyUnitPersistencePort).removeSurveyUnitFromSample(1l, 1l);
        }
        catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }
    }

    @Test
    void removeSurveyUnitFromSample_throw() {
        when(samplePersistencePort.existsById(Mockito.anyLong())).thenReturn(false);
        Assertions.assertThrows(SampleNotFoundException.class, () -> surveyUnitService.removeSurveyUnitFromSample(1l, Mockito.anyLong()));
        when(samplePersistencePort.existsById(1l)).thenReturn(true);
        when(surveyUnitPersistencePort.existsById(Mockito.anyLong())).thenReturn(false);
        Assertions.assertThrows(SurveyUnitNotFoundException.class, () -> surveyUnitService.removeSurveyUnitFromSample(Mockito.anyLong(), 1l));
    }

    @Test
    void getSurveyUnitById_does_not_throw() {

        Optional<SurveyUnitDto> suDto = Optional.of(SurveyUnitDto.builder().repositoryId(1l).build());

        when(surveyUnitPersistencePort.findById(1l)).thenReturn(suDto);

        try {
            SurveyUnitDto su = surveyUnitService.getSurveyUnitById(1l);
            verify(surveyUnitPersistencePort).findById(1l);
            Assertions.assertNotNull(su.getRepositoryId());
        }
        catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }

    }

    @Test
    void getSurveyUnitById_throw() {
        when(surveyUnitPersistencePort.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(SurveyUnitNotFoundException.class, () -> surveyUnitService.getSurveyUnitById(Mockito.anyLong()));
    }

    @Test
    void getSurveyUnitsBySampleId_does_not_throw() {

        SampleDto sample = SampleDto.builder().id(1l).build();
        SurveyUnitDto su1 = SurveyUnitDto.builder().repositoryId(1l).build();
        SurveyUnitDto su2 = SurveyUnitDto.builder().repositoryId(2l).build();

        List<SampleSurveyUnitDto> ssuList = new ArrayList<>();
        ssuList.add(SampleSurveyUnitDto.builder().sample(sample).surveyUnit(su1).build());
        ssuList.add(SampleSurveyUnitDto.builder().sample(sample).surveyUnit(su2).build());

        when(samplePersistencePort.existsById(1l)).thenReturn(true);

        when(sampleSurveyUnitPersistencePort.findSurveyUnitsBySampleId(1l)).thenReturn(ssuList);

        try {
            List<SampleSurveyUnitDto> listSsu = surveyUnitService.getSurveyUnitsBySampleId(1l);
            verify(samplePersistencePort).existsById(1l);
            verify(sampleSurveyUnitPersistencePort).findSurveyUnitsBySampleId(1l);
            Assertions.assertEquals(2, listSsu.size());
            Assertions.assertEquals(1l, listSsu.get(0).getSurveyUnit().getRepositoryId());
            Assertions.assertEquals(2l, listSsu.get(1).getSurveyUnit().getRepositoryId());
            Assertions.assertEquals(1l, listSsu.get(0).getSample().getId());
        }
        catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }
    }

    @Test
    void getSurveyUnitsBySampleId_throw() {
        when(samplePersistencePort.existsById(Mockito.anyLong())).thenReturn(false);
        Assertions.assertThrows(SampleNotFoundException.class, () -> surveyUnitService.getSurveyUnitsBySampleId(Mockito.anyLong()));
    }

    @Test
    void getSurveyUnitIdsBySampleId_does_not_throw() {

        List<Long> ids = List.of(1l, 2l, 3l);

        when(samplePersistencePort.existsById(1l)).thenReturn(true);

        when(sampleSurveyUnitPersistencePort.findAllIdsBySampleId(1l)).thenReturn(ids);

        try {
            List<Long> listIds = surveyUnitService.getSurveyUnitIdsBySampleId(1l);
            verify(samplePersistencePort).existsById(1l);
            verify(sampleSurveyUnitPersistencePort).findAllIdsBySampleId(1l);
            Assertions.assertEquals(3, listIds.size());
            Assertions.assertEquals(1l, listIds.get(0));
            Assertions.assertEquals(2l, listIds.get(1));
            Assertions.assertEquals(3l, listIds.get(2));
        }
        catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }
    }

    @Test
    void getSurveyUnitIdsBySampleId_throw() {
        when(samplePersistencePort.existsById(Mockito.anyLong())).thenReturn(false);
        Assertions.assertThrows(SampleNotFoundException.class, () -> surveyUnitService.getSurveyUnitIdsBySampleId(Mockito.anyLong()));
    }

}
