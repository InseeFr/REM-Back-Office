package fr.insee.rem.domain.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import fr.insee.rem.domain.dtos.SampleDto;
import fr.insee.rem.domain.dtos.SampleSurveyUnitDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.dtos.TypeUnit;
import fr.insee.rem.domain.exception.SampleNotFoundException;
import fr.insee.rem.domain.exception.SettingsException;
import fr.insee.rem.domain.exception.SurveyUnitNotFoundException;
import fr.insee.rem.domain.exception.SurveyUnitsNotFoundException;
import fr.insee.rem.domain.ports.spi.SamplePersistencePort;
import fr.insee.rem.domain.ports.spi.SampleSurveyUnitPersistencePort;
import fr.insee.rem.domain.ports.spi.SurveyUnitPersistencePort;
import fr.insee.rem.domain.records.SuIdMappingRecord;

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

    private List<SurveyUnitDto> emptyList;
    private List<SurveyUnitDto> twoSurveyUnits;
    private SampleSurveyUnitDto ssuDto;
    private Optional<SurveyUnitDto> suDto;
    private List<SampleSurveyUnitDto> ssuList;
    private List<Long> ids;
    private List<Long> emptyListIds;

    List<SuIdMappingRecord> idsMappind;

    @BeforeEach
    void init() {
        emptyList = List.of();

        SurveyUnitDto su1 = SurveyUnitDto.builder().typeUnit(TypeUnit.HOUSEHOLD).build();
        SurveyUnitDto su2 = SurveyUnitDto.builder().typeUnit(TypeUnit.HOUSEHOLD).build();
        twoSurveyUnits = List.of(su1, su2);

        Optional<SampleDto> sampleDto = Optional.of(SampleDto.builder().id(1l).label("test").build());
        suDto = Optional.of(SurveyUnitDto.builder().repositoryId(1l).build());
        ssuDto = SampleSurveyUnitDto.builder().sample(sampleDto.get()).surveyUnit(suDto.get()).build();

        Optional<SurveyUnitDto> suDto2 = Optional.of(SurveyUnitDto.builder().repositoryId(2l).build());
        SampleSurveyUnitDto ssuDto2 = SampleSurveyUnitDto.builder().sample(sampleDto.get()).surveyUnit(suDto2.get())
            .build();
        ssuList = List.of(ssuDto, ssuDto2);

        ids = List.of(1l, 2l, 3l);

        idsMappind = new ArrayList<>();
        idsMappind.add(new SuIdMappingRecord(1l, "AAA"));
        idsMappind.add(new SuIdMappingRecord(2l, "BBB"));

        emptyListIds = List.of();
    }

    @Test
    void importSurveyUnitsToSample_sample_existsById_throw() {
        when(samplePersistencePort.existsById(1l)).thenReturn(false);
        
        Assertions.assertThrows(SampleNotFoundException.class,
              () -> surveyUnitService.importSurveyUnitsToSample(1l, twoSurveyUnits));
    }

    @Test
    void importSurveyUnitsToSample_sampleId_null_throw() {
        Assertions.assertThrows(SampleNotFoundException.class, () -> surveyUnitService
            .importSurveyUnitsToSample(null, twoSurveyUnits));
    }

    @Test
    void importSurveyUnitsToSample_empty_or_null_list() {
        Assertions.assertThrows(SettingsException.class, () -> surveyUnitService
            .importSurveyUnitsToSample(1l, emptyList));
        Assertions.assertThrows(SettingsException.class, () -> surveyUnitService
            .importSurveyUnitsToSample(1l, null));
    }

    @Test
    void importSurveyUnitsToSample_saveAll() {       

        when(samplePersistencePort.existsById(1l)).thenReturn(true);

        when(sampleSurveyUnitPersistencePort.saveAll(1l, twoSurveyUnits)).then(new Answer<List<SampleSurveyUnitDto>>() {
            long sequence = 1l;

            SampleDto sample1 = SampleDto.builder().id(1l).label("sample1").build();

            @Override
            public List<SampleSurveyUnitDto> answer(InvocationOnMock invocation) throws Throwable {
                List<SurveyUnitDto> suList = invocation.getArgument(1);
                List<SampleSurveyUnitDto> ssuList = new ArrayList<>();
                suList.stream().forEach(su -> {
                    su.setRepositoryId(sequence++);
                    SampleSurveyUnitDto ssu = SampleSurveyUnitDto.builder().sample(sample1).surveyUnit(su).build();
                    ssuList.add(ssu);
                });

                return ssuList;
            }
        });

        try {
            List<SampleSurveyUnitDto> insertedListSsu = surveyUnitService.importSurveyUnitsToSample(1l, twoSurveyUnits);
            verify(samplePersistencePort).existsById(1l);
            verify(sampleSurveyUnitPersistencePort).saveAll(1l, twoSurveyUnits);
            Assertions.assertNotNull(insertedListSsu.get(0).getSurveyUnit().getRepositoryId());
            Assertions.assertEquals(1l, insertedListSsu.get(0).getSurveyUnit().getRepositoryId());
            Assertions.assertEquals(2l, insertedListSsu.get(1).getSurveyUnit().getRepositoryId());
            Assertions.assertEquals(1l, insertedListSsu.get(0).getSample().getId());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }
    }

    @Test
    void addSurveyUnitToSample_sample_existsById_throw() {
        when(samplePersistencePort.existsById(1l)).thenReturn(false);
        
        Assertions.assertThrows(SampleNotFoundException.class,
              () -> surveyUnitService.addSurveyUnitToSample(1l, 1l));
    }

    @Test
    void addSurveyUnitToSample_sampleId_null_throw() {
        Assertions.assertThrows(SampleNotFoundException.class, () -> surveyUnitService
            .addSurveyUnitToSample(1l, null));
    }

    @Test
    void addSurveyUnitToSample_surveyUnit_existsById_throw() {
        when(surveyUnitPersistencePort.existsById(1l)).thenReturn(false);
        when(samplePersistencePort.existsById(1l)).thenReturn(true);
        Assertions.assertThrows(SurveyUnitNotFoundException.class,
              () -> surveyUnitService.addSurveyUnitToSample(1l, 1l));
    }

    @Test
    void addSurveyUnitToSample_surveyUnitId_null_throw() {
        when(samplePersistencePort.existsById(1l)).thenReturn(true);
        Assertions.assertThrows(SurveyUnitNotFoundException.class, () -> surveyUnitService
            .addSurveyUnitToSample(null, 1l));
    }

    @Test
    void addSurveyUnitToSample_does_not_throw() {
        when(samplePersistencePort.existsById(1l)).thenReturn(true);
        when(surveyUnitPersistencePort.existsById(1l)).thenReturn(true);        
        when(sampleSurveyUnitPersistencePort.addSurveyUnitToSample(1l, 1l)).thenReturn(ssuDto);

        try {
            SampleSurveyUnitDto insertedSsu = surveyUnitService.addSurveyUnitToSample(1l, 1l);
            verify(samplePersistencePort).existsById(1l);
            verify(surveyUnitPersistencePort).existsById(1l);
            verify(sampleSurveyUnitPersistencePort).addSurveyUnitToSample(1l, 1l);
            Assertions.assertEquals(1l, insertedSsu.getSurveyUnit().getRepositoryId());
            Assertions.assertEquals(1l, insertedSsu.getSample().getId());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }
    }

    @Test
    void deleteSurveyUnitById_surveyUnit_existsById_throw() {
        when(surveyUnitPersistencePort.existsById(1l)).thenReturn(false);
        Assertions.assertThrows(SurveyUnitNotFoundException.class,
              () -> surveyUnitService.deleteSurveyUnitById(1l));
    }

    @Test
    void deleteSurveyUnitById_surveyUnitId_null_throw() {
        Assertions.assertThrows(SurveyUnitNotFoundException.class, () -> surveyUnitService
            .deleteSurveyUnitById(null));
    }


    @Test
    void deleteSurveyUnitById_delete() {

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
    void removeSurveyUnitFromSample_sample_existsById_throw() {
        when(samplePersistencePort.existsById(1l)).thenReturn(false);
        
        Assertions.assertThrows(SampleNotFoundException.class,
              () -> surveyUnitService.removeSurveyUnitFromSample(1l, 1l));
    }

    @Test
    void removeSurveyUnitFromSample_sampleId_null_throw() {
        Assertions.assertThrows(SampleNotFoundException.class, () -> surveyUnitService
            .removeSurveyUnitFromSample(1l, null));
    }

    @Test
    void removeSurveyUnitFromSample_surveyUnit_existsById_throw() {
        when(surveyUnitPersistencePort.existsById(1l)).thenReturn(false);
        when(samplePersistencePort.existsById(1l)).thenReturn(true);
        Assertions.assertThrows(SurveyUnitNotFoundException.class,
              () -> surveyUnitService.removeSurveyUnitFromSample(1l, 1l));
    }

    @Test
    void removeSurveyUnitFromSample_surveyUnitId_null_throw() {
        when(samplePersistencePort.existsById(1l)).thenReturn(true);
        Assertions.assertThrows(SurveyUnitNotFoundException.class, () -> surveyUnitService
            .removeSurveyUnitFromSample(null, 1l));
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
    void getSurveyUnitById_surveyUnit_findById_throw() {
        when(surveyUnitPersistencePort.findById(1l)).thenReturn(Optional.empty());
        Assertions.assertThrows(SurveyUnitNotFoundException.class,
              () -> surveyUnitService.getSurveyUnitById(1l));
    }

    @Test
    void getSurveyUnitById_surveyUnitId_null_throw() {
        Assertions.assertThrows(SurveyUnitNotFoundException.class, () -> surveyUnitService
            .getSurveyUnitById(null));
    }


    @Test
    void getSurveyUnitById_does_not_throw() {

        when(surveyUnitPersistencePort.findById(1l)).thenReturn(suDto);

        try {
            SurveyUnitDto su = surveyUnitService.getSurveyUnitById(1l);
            verify(surveyUnitPersistencePort).findById(1l);
            Assertions.assertNotNull(su.getRepositoryId());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }

    }

    @Test
    void getSurveyUnitsBySampleId_sample_existsById_throw() {
        when(samplePersistencePort.existsById(1l)).thenReturn(false);
        
        Assertions.assertThrows(SampleNotFoundException.class,
              () -> surveyUnitService.getSurveyUnitsBySampleId(1l));
    }

    @Test
    void getSurveyUnitsBySampleId_sampleId_null_throw() {
        Assertions.assertThrows(SampleNotFoundException.class, () -> surveyUnitService
            .getSurveyUnitsBySampleId(null));
    }


    @Test
    void getSurveyUnitsBySampleId_does_not_throw() {

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
        } catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }
    }


    @Test
    void getSurveyUnitIdsBySampleId_does_not_throw() {

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
        } catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }
    }

    @Test
    void getSurveyUnitIdsBySampleId_sample_existsById_throw() {
        when(samplePersistencePort.existsById(1l)).thenReturn(false);
        
        Assertions.assertThrows(SampleNotFoundException.class,
              () -> surveyUnitService.getSurveyUnitIdsBySampleId(1l));
    }

    @Test
    void getSurveyUnitIdsBySampleId_sampleId_null_throw() {
        Assertions.assertThrows(SampleNotFoundException.class, () -> surveyUnitService
            .getSurveyUnitIdsBySampleId(null));
    }

    @Test
    void getIdMappingTableBySampleId_sample_existsById_throw() {
        when(samplePersistencePort.existsById(1l)).thenReturn(false);
        
        Assertions.assertThrows(SampleNotFoundException.class,
              () -> surveyUnitService.getIdMappingTableBySampleId(1l));
    }

    @Test
    void getIdMappingTableBySampleId_sampleId_null_throw() {
        Assertions.assertThrows(SampleNotFoundException.class, () -> surveyUnitService
            .getIdMappingTableBySampleId(null));
    }

    @Test
    void getIdMappingTableBySampleId_does_not_throw() {
        when(samplePersistencePort.existsById(1l)).thenReturn(true);     
        when(sampleSurveyUnitPersistencePort.findSuIdMappingBySampleId(1l)).thenReturn(idsMappind);
        try {
            List<SuIdMappingRecord> mapping = surveyUnitService.getIdMappingTableBySampleId(1l);
            Assertions.assertEquals(2, mapping.size());
            Assertions.assertEquals(1l, mapping.get(0).repositoryId());
            Assertions.assertEquals("AAA", mapping.get(0).externalId());
            Assertions.assertEquals(2l, mapping.get(1).repositoryId());
            Assertions.assertEquals("BBB", mapping.get(1).externalId());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }
    }

    @Test
    void addSurveyUnitsToSample_sample_existsById_throw() {
        when(samplePersistencePort.existsById(1l)).thenReturn(false);
        
        Assertions.assertThrows(SampleNotFoundException.class,
              () -> surveyUnitService.addSurveyUnitsToSample(ids,1l));
    }

    @Test
    void addSurveyUnitsToSample_sampleId_null_throw() {
        Assertions.assertThrows(SampleNotFoundException.class, () -> surveyUnitService
            .addSurveyUnitsToSample(ids, null));
    }

    @Test
    void addSurveyUnitsToSample_empty_or_null_list() {
        Assertions.assertThrows(SettingsException.class, () -> surveyUnitService
            .addSurveyUnitsToSample(emptyListIds, 1l));
        Assertions.assertThrows(SettingsException.class, () -> surveyUnitService
            .addSurveyUnitsToSample(null, 1l));

    }

    @Test
    void addSurveyUnitsToSample_does_not_throw(){

        when(samplePersistencePort.existsById(1l)).thenReturn(true);
        when(surveyUnitPersistencePort.existsById(1l)).thenReturn(true);
        when(surveyUnitPersistencePort.existsById(2l)).thenReturn(true);
        when(surveyUnitPersistencePort.existsById(3l)).thenReturn(true);
        when(sampleSurveyUnitPersistencePort.addSurveyUnitToSample(1l, 1l)).thenReturn(ssuDto);
        when(sampleSurveyUnitPersistencePort.addSurveyUnitToSample(2l, 1l)).thenReturn(ssuDto);
        when(sampleSurveyUnitPersistencePort.addSurveyUnitToSample(3l, 1l)).thenReturn(ssuDto);
        try {
            int count = surveyUnitService.addSurveyUnitsToSample(ids, 1l);
            Assertions.assertEquals(3, count);
            verify(samplePersistencePort).existsById(1l);
            verify(surveyUnitPersistencePort).existsById(1l);
            verify(surveyUnitPersistencePort).existsById(2l);
            verify(surveyUnitPersistencePort).existsById(3l);
            verify(sampleSurveyUnitPersistencePort).addSurveyUnitToSample(1l,1l);
            verify(sampleSurveyUnitPersistencePort).addSurveyUnitToSample(2l,1l);
            verify(sampleSurveyUnitPersistencePort).addSurveyUnitToSample(3l,1l);
        } catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }
    }

    @Test
    void addSurveyUnitsToSample_surveyUnit_existsById_throw(){

        when(samplePersistencePort.existsById(1l)).thenReturn(true);
        when(surveyUnitPersistencePort.existsById(1l)).thenReturn(true);
        when(surveyUnitPersistencePort.existsById(2l)).thenReturn(true);
        when(surveyUnitPersistencePort.existsById(3l)).thenReturn(false);
        Assertions.assertThrows(SurveyUnitsNotFoundException.class, () -> 
            surveyUnitService.addSurveyUnitsToSample(ids, 1l));
        
    }

    @Test
    void checkRepositoryId_empty_or_null_list() {
        boolean check = surveyUnitService.checkRepositoryId(List.of());
        Assertions.assertFalse(check);
        boolean check2 = surveyUnitService.checkRepositoryId(null);
        Assertions.assertFalse(check2);
    }

    @Test
    void checkRepositoryId_list_with_at_least_one_id() {
        SurveyUnitDto su1 = SurveyUnitDto.builder().repositoryId(1l).build();
        SurveyUnitDto su2 = SurveyUnitDto.builder().repositoryId(2l).build();
        SurveyUnitDto su3 = SurveyUnitDto.builder().build();
        boolean check = surveyUnitService.checkRepositoryId(List.of(su1, su2, su3));
        Assertions.assertTrue(check);
    }

    @Test
    void checkRepositoryId_list_without_id() {
        SurveyUnitDto su1 = SurveyUnitDto.builder().build();
        SurveyUnitDto su2 = SurveyUnitDto.builder().build();
        SurveyUnitDto su3 = SurveyUnitDto.builder().build();
        boolean check = surveyUnitService.checkRepositoryId(List.of(su1, su2, su3));
        Assertions.assertFalse(check);
    }

    @Test
    void updateSurveyUnit_empty_data() {
        Assertions.assertThrows(SettingsException.class, () -> surveyUnitService
            .updateSurveyUnit(null));
    }

    @Test
    void updateSurveyUnit_empty_id() {
        SurveyUnitDto suDto = SurveyUnitDto.builder().build();
        Assertions.assertThrows(SettingsException.class, () -> surveyUnitService
            .updateSurveyUnit(suDto));
    }

    @Test
    void updateSurveyUnit_existsById_throw() {
        SurveyUnitDto suDto = SurveyUnitDto.builder().repositoryId(1l).build();
        when(surveyUnitPersistencePort.existsById(1l)).thenReturn(false);
        Assertions.assertThrows(SurveyUnitNotFoundException.class, () -> surveyUnitService.updateSurveyUnit(suDto));
    }

    @Test
    void updateSurveyUnit_does_not_throw() {
        SurveyUnitDto suDto = SurveyUnitDto.builder().repositoryId(1l).build();
        when(surveyUnitPersistencePort.existsById(1l)).thenReturn(true);
        when(surveyUnitPersistencePort.update(suDto)).thenReturn(suDto);
        try {
            SurveyUnitDto su = surveyUnitService.updateSurveyUnit(suDto);
            Assertions.assertNotNull(su);
            Assertions.assertEquals(1l, su.getRepositoryId());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown");
        }
    }

}
