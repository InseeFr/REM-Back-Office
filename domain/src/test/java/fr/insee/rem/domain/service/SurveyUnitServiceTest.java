package fr.insee.rem.domain.service;

import fr.insee.rem.domain.dtos.SampleDto;
import fr.insee.rem.domain.dtos.SampleSurveyUnitDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.dtos.TypeUnit;
import fr.insee.rem.domain.exception.SampleNotFoundException;
import fr.insee.rem.domain.exception.SettingsException;
import fr.insee.rem.domain.exception.SurveyUnitNotFoundException;
import fr.insee.rem.domain.exception.SurveyUnitsNotFoundException;
import fr.insee.rem.domain.ports.api.SampleServicePort;
import fr.insee.rem.domain.ports.api.SurveyUnitServicePort;
import fr.insee.rem.domain.records.SuIdMappingRecord;
import fr.insee.rem.domain.service.mock.SamplePersistenceInMemory;
import fr.insee.rem.domain.service.mock.SampleSurveyUnitPersistenceInMemory;
import fr.insee.rem.domain.service.mock.SurveyUnitPersistenceInMemory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class SurveyUnitServiceTest {
    SamplePersistenceInMemory samplePersistenceInMemory = new SamplePersistenceInMemory();
    SurveyUnitPersistenceInMemory surveyUnitPersistenceInMemory = new SurveyUnitPersistenceInMemory();
    SampleSurveyUnitPersistenceInMemory sampleSurveyUnitPersistenceInMemory =
            new SampleSurveyUnitPersistenceInMemory(samplePersistenceInMemory, surveyUnitPersistenceInMemory);

    SurveyUnitServicePort surveyUnitService = new SurveyUnitServiceImpl(surveyUnitPersistenceInMemory,
            sampleSurveyUnitPersistenceInMemory, samplePersistenceInMemory);
    SampleServicePort sampleService = new SampleServiceImpl(samplePersistenceInMemory);

    private List<SampleSurveyUnitDto> initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits(String label) {
        SampleDto existingSample = sampleService.createSample(label);
        List<SurveyUnitDto> surveyUnitsToImport =
                List.of(SurveyUnitDto.builder().typeUnit(TypeUnit.HOUSEHOLD).externalId("00001").build(),
                        SurveyUnitDto.builder().typeUnit(TypeUnit.HOUSEHOLD).externalId("00002").build());
        return surveyUnitService.importSurveyUnitsToSample(existingSample.getId(), surveyUnitsToImport);
    }

    @Test
    void shouldImportListOfSurveyUnitsAndLinkThemToExistingSample() {
        // Given
        SampleDto existingSample = sampleService.createSample("existing sample");
        List<SurveyUnitDto> surveyUnitsToImport =
                List.of(SurveyUnitDto.builder().typeUnit(TypeUnit.HOUSEHOLD).externalId("00001").build(),
                        SurveyUnitDto.builder().typeUnit(TypeUnit.HOUSEHOLD).externalId("00002").build());

        // When
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                surveyUnitService.importSurveyUnitsToSample(existingSample.getId(), surveyUnitsToImport);

        // Then
        Assertions.assertNotNull(importedSampleSurveyUnits);
        Assertions.assertEquals(2, importedSampleSurveyUnits.size());
        Assertions.assertEquals(1L, importedSampleSurveyUnits.get(0).getSurveyUnit().getRepositoryId());
        Assertions.assertEquals(2L, importedSampleSurveyUnits.get(1).getSurveyUnit().getRepositoryId());
        Assertions.assertEquals("00001", importedSampleSurveyUnits.get(0).getSurveyUnit().getExternalId());
        Assertions.assertEquals("00002", importedSampleSurveyUnits.get(1).getSurveyUnit().getExternalId());
        Assertions.assertEquals(1L, importedSampleSurveyUnits.get(0).getSample().getId());
        Assertions.assertEquals(1L, importedSampleSurveyUnits.get(1).getSample().getId());
    }

    @Test
    void shouldReturnSampleNotFoundExceptionWhenImportSurveyUnitsIntoNotExistingSample() {
        // Given
        List<SurveyUnitDto> surveyUnitsToImport =
                List.of(SurveyUnitDto.builder().typeUnit(TypeUnit.HOUSEHOLD).externalId("00001").build(),
                        SurveyUnitDto.builder().typeUnit(TypeUnit.HOUSEHOLD).externalId("00002").build());
        Long notExistingSampleId = 99L;

        // When + Then
        SampleNotFoundException exception =
                Assertions.assertThrows(SampleNotFoundException.class,
                        () -> surveyUnitService.importSurveyUnitsToSample(notExistingSampleId, surveyUnitsToImport));
        Assertions.assertEquals(String.format("Sample [%s] doesn't exist", notExistingSampleId),
                exception.getMessage());

    }

    @Test
    void shouldReturnSettingsExceptionWhenImportNullListToExistingSample() {
        // Given
        Long existingSampleId = 1L;

        // When + Then
        SettingsException exception = Assertions.assertThrows(SettingsException.class,
                () -> surveyUnitService.importSurveyUnitsToSample(existingSampleId, null));
        Assertions.assertEquals("List of survey units to import is empty or null", exception.getMessage());
    }

    @Test
    void shouldReturnSettingsExceptionWhenImportEmptyListToExistingSample() {
        // Given
        List<SurveyUnitDto> emptyList = List.of();
        Long existingSampleId = 1L;

        // When + Then
        SettingsException exception = Assertions.assertThrows(SettingsException.class,
                () -> surveyUnitService.importSurveyUnitsToSample(existingSampleId, emptyList));
        Assertions.assertEquals("List of survey units to import is empty or null", exception.getMessage());
    }

    @Test
    void shouldAddAnExistingSurveyUnitIntoAnotherSample() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        Long existingSurveyUnitId = importedSampleSurveyUnits.get(0).getSurveyUnit().getRepositoryId();
        SampleDto anotherSample = sampleService.createSample("new sample");
        Long anotherSampleId = anotherSample.getId();

        // When
        SampleSurveyUnitDto addedSampleSurveyUnit = surveyUnitService.addSurveyUnitToSample(existingSurveyUnitId,
                anotherSampleId);

        // Then
        Assertions.assertNotNull(addedSampleSurveyUnit);
        Assertions.assertEquals(existingSurveyUnitId, addedSampleSurveyUnit.getSurveyUnit().getRepositoryId());
        Assertions.assertEquals(anotherSampleId, addedSampleSurveyUnit.getSample().getId());
    }

    @Test
    void shouldReturnSampleNotFoundExceptionWhenAddExistingSurveyUnitIntoNonExistentSample() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        Long existingSurveyUnitId = importedSampleSurveyUnits.get(0).getSurveyUnit().getRepositoryId();
        Long notExistingSampleId = 99L;

        // When + Then
        SampleNotFoundException exception = Assertions.assertThrows(SampleNotFoundException.class,
                () -> surveyUnitService.addSurveyUnitToSample(existingSurveyUnitId, notExistingSampleId));
        Assertions.assertEquals(String.format("Sample [%s] doesn't exist", notExistingSampleId),
                exception.getMessage());
    }

    @Test
    void shouldReturnSurveyUnitNotFoundExceptionWhenAddNotExistingSurveyUnitIntoExistingSample() {
        // Given
        SampleDto existingSample = sampleService.createSample("existing sample");
        Long existingSampleId = existingSample.getId();
        Long notExistingSurveyUnitId = 99L;

        // When + Then
        SurveyUnitNotFoundException exception = Assertions.assertThrows(SurveyUnitNotFoundException.class,
                () -> surveyUnitService.addSurveyUnitToSample(notExistingSurveyUnitId, existingSampleId));
        Assertions.assertEquals(String.format("SurveyUnit [%s] doesn't exist", notExistingSurveyUnitId),
                exception.getMessage());
    }

    @Test
    void shouldDeleteSurveyUnit() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        Long surveyUnitIdToDelete = importedSampleSurveyUnits.get(0).getSurveyUnit().getRepositoryId();

        // When
        surveyUnitService.deleteSurveyUnitById(surveyUnitIdToDelete);

        // Then
        Assertions.assertFalse(surveyUnitPersistenceInMemory.existsById(surveyUnitIdToDelete));
    }


    @Test
    void shouldReturnSurveyUnitNotFoundExceptionWhenDeleteNotExistingSurveyUnit() {
        //Given
        Long notExistingSurveyUnitId = 99L;

        // When + Then
        SurveyUnitNotFoundException exception = Assertions.assertThrows(SurveyUnitNotFoundException.class,
                () -> surveyUnitService.deleteSurveyUnitById(notExistingSurveyUnitId));
        Assertions.assertEquals(String.format("SurveyUnit [%s] doesn't exist", notExistingSurveyUnitId),
                exception.getMessage());
    }

    @Test
    void shouldRemoveSurveyUnitFromSample() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        Long surveyUnitIdToRemove = importedSampleSurveyUnits.get(0).getSurveyUnit().getRepositoryId();
        Long existingSampleId = importedSampleSurveyUnits.get(0).getSample().getId();

        // When
        surveyUnitService.removeSurveyUnitFromSample(surveyUnitIdToRemove, existingSampleId);

        // Then
        Assertions.assertEquals(1,
                sampleSurveyUnitPersistenceInMemory.findSurveyUnitsBySampleId(existingSampleId).size());
    }

    @Test
    void shouldReturnSampleNotFoundExceptionWhenRemoveExistingSurveyUnitFromNotExistingSample() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        Long existingSurveyUnitId = importedSampleSurveyUnits.get(0).getSurveyUnit().getRepositoryId();
        Long notExistingSampleId = 99L;

        // When + Then
        SampleNotFoundException exception = Assertions.assertThrows(SampleNotFoundException.class,
                () -> surveyUnitService.removeSurveyUnitFromSample(existingSurveyUnitId, notExistingSampleId));
        Assertions.assertEquals(String.format("Sample [%s] doesn't exist", notExistingSampleId),
                exception.getMessage());
    }

    @Test
    void shouldReturnSurveyUnitNotFoundExceptionWhenRemoveNotExistingSurveyUnitFromExistingSample() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        Long existingSampleId = importedSampleSurveyUnits.get(0).getSample().getId();
        Long notExistingSurveyUnitId = 99L;

        // When + Then
        SurveyUnitNotFoundException exception = Assertions.assertThrows(SurveyUnitNotFoundException.class,
                () -> surveyUnitService.removeSurveyUnitFromSample(notExistingSurveyUnitId, existingSampleId));
        Assertions.assertEquals(String.format("SurveyUnit [%s] doesn't exist", notExistingSurveyUnitId),
                exception.getMessage());
    }

    @Test
    void shouldReturnSurveyUnitFromId() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        Long existingSurveyUnitId = importedSampleSurveyUnits.get(0).getSurveyUnit().getRepositoryId();

        // When
        SurveyUnitDto existingSurveyUnit = surveyUnitService.getSurveyUnitById(existingSurveyUnitId);

        // Then
        Assertions.assertNotNull(existingSurveyUnit);
        Assertions.assertEquals("00001", existingSurveyUnit.getExternalId());
        Assertions.assertEquals(existingSurveyUnitId, existingSurveyUnit.getRepositoryId());
    }

    @Test
    void shouldReturnSurveyUnitNotFoundExceptionWhenGetNotExistingSurveyUnit() {
        // Given
        Long notExistingSurveyUnitId = 99L;

        // When + Then
        SurveyUnitNotFoundException exception = Assertions.assertThrows(SurveyUnitNotFoundException.class,
                () -> surveyUnitService.getSurveyUnitById(notExistingSurveyUnitId));
        Assertions.assertEquals(String.format("SurveyUnit [%s] doesn't exist", notExistingSurveyUnitId),
                exception.getMessage());
    }

    @Test
    void shouldReturnSampleNotFoundExceptionWhenGetListOfSurveyUnitsFromNotExistingSample() {
        // Given
        Long notExistingSampleId = 99L;

        // When + Then
        SampleNotFoundException exception = Assertions.assertThrows(SampleNotFoundException.class,
                () -> surveyUnitService.getSurveyUnitsBySampleId(notExistingSampleId));
        Assertions.assertEquals(String.format("Sample [%s] doesn't exist", notExistingSampleId),
                exception.getMessage());
    }


    @Test
    void shouldReturnListOfSurveyUnitsLinkToExistingSample() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        List<SampleSurveyUnitDto> otherImportedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("other sample");
        List<Long> idsNotInRetournedList = otherImportedSampleSurveyUnits.stream()
                .map(SampleSurveyUnitDto::getSurveyUnit)
                .map(SurveyUnitDto::getRepositoryId)
                .toList();
        Long importSampleId = importedSampleSurveyUnits.get(0).getSample().getId();

        // When
        List<SampleSurveyUnitDto> returnedList = surveyUnitService.getSurveyUnitsBySampleId(importSampleId);

        // Then
        Assertions.assertNotNull(returnedList);
        Assertions.assertEquals(importedSampleSurveyUnits.size(), returnedList.size());
        for (int i = 0; i < returnedList.size(); i++) {
            Assertions.assertEquals(importedSampleSurveyUnits.get(i).getSurveyUnit().getRepositoryId(),
                    returnedList.get(i).getSurveyUnit().getRepositoryId());
            Assertions.assertEquals(importedSampleSurveyUnits.get(i).getSample().getId(),
                    returnedList.get(i).getSample().getId());
            Assertions.assertFalse(idsNotInRetournedList.contains(returnedList.get(i).getSurveyUnit().getRepositoryId()));
        }
    }

    @Test
    void shouldReturnSampleNotFoundExceptionWhenGetListOfSurveyUnitIdsFromNotExistingSample() {
        // Given
        Long notExistingSampleId = 99L;

        // When + Then
        SampleNotFoundException exception = Assertions.assertThrows(SampleNotFoundException.class,
                () -> surveyUnitService.getSurveyUnitIdsBySampleId(notExistingSampleId));
        Assertions.assertEquals(String.format("Sample [%s] doesn't exist", notExistingSampleId),
                exception.getMessage());
    }


    @Test
    void shouldReturnListOfSurveyUnitIdsLinkToExistingSample() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        List<SampleSurveyUnitDto> otherImportedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("other sample");
        List<Long> idsNotInRetournedList = otherImportedSampleSurveyUnits.stream()
                .map(SampleSurveyUnitDto::getSurveyUnit)
                .map(SurveyUnitDto::getRepositoryId)
                .toList();
        Long importSampleId = importedSampleSurveyUnits.get(0).getSample().getId();

        // When
        List<Long> returnedIds = surveyUnitService.getSurveyUnitIdsBySampleId(importSampleId);

        // Then
        Assertions.assertNotNull(returnedIds);
        Assertions.assertEquals(importedSampleSurveyUnits.size(), returnedIds.size());
        for (int i = 0; i < returnedIds.size(); i++) {
            Assertions.assertEquals(importedSampleSurveyUnits.get(i).getSurveyUnit().getRepositoryId(),
                    returnedIds.get(i));
            Assertions.assertFalse(idsNotInRetournedList.contains(returnedIds.get(i)));
        }
    }

    @Test
    void shouldReturnSampleNotFoundExceptionWhenGetListOfSurveyUnitIdsMappingTableFromNotExistingSample() {
        // Given
        Long notExistingSampleId = 99L;

        // When + Then
        SampleNotFoundException exception = Assertions.assertThrows(SampleNotFoundException.class,
                () -> surveyUnitService.getIdMappingTableBySampleId(notExistingSampleId));
        Assertions.assertEquals(String.format("Sample [%s] doesn't exist", notExistingSampleId),
                exception.getMessage());
    }

    @Test
    void shouldReturnSurveyUnitIdsMappingTableLinkToExistingSample() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        List<SampleSurveyUnitDto> otherImportedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("other sample");
        List<Long> idsNotInRetournedList = otherImportedSampleSurveyUnits.stream()
                .map(SampleSurveyUnitDto::getSurveyUnit)
                .map(SurveyUnitDto::getRepositoryId)
                .toList();
        Long importSampleId = importedSampleSurveyUnits.get(0).getSample().getId();

        // When
        List<SuIdMappingRecord> suIdsMapping = surveyUnitService.getIdMappingTableBySampleId(importSampleId);

        // Then
        Assertions.assertNotNull(suIdsMapping);
        Assertions.assertEquals(importedSampleSurveyUnits.size(), suIdsMapping.size());
        for (int i = 0; i < suIdsMapping.size(); i++) {
            Assertions.assertEquals(importedSampleSurveyUnits.get(i).getSurveyUnit().getRepositoryId(),
                    suIdsMapping.get(i).repositoryId());
            Assertions.assertEquals(importedSampleSurveyUnits.get(i).getSurveyUnit().getExternalId(),
                    suIdsMapping.get(i).externalId());
            Assertions.assertFalse(idsNotInRetournedList.contains(suIdsMapping.get(i).repositoryId()));
        }


    }

    @Test
    void shouldReturnSampleNotFoundExceptionWhenAddListOfSurveyUnitsIntoNotExistingSample() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        List<Long> existingSurveyUnits = importedSampleSurveyUnits.stream()
                .map(SampleSurveyUnitDto::getSurveyUnit)
                .map(SurveyUnitDto::getRepositoryId)
                .toList();
        Long notExistingSampleId = 99L;

        // When + Then
        SampleNotFoundException exception = Assertions.assertThrows(SampleNotFoundException.class,
                () -> surveyUnitService.addSurveyUnitsToSample(existingSurveyUnits, notExistingSampleId));
        Assertions.assertEquals(String.format("Sample [%s] doesn't exist", notExistingSampleId),
                exception.getMessage());
    }

    @Test
    void shouldReturnSettingsExceptionWhenAddEmptyListIntoExistingSample() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        Long existingSampleId = importedSampleSurveyUnits.get(0).getSample().getId();
        List<Long> emptyList = List.of();

        // When + Then
        SettingsException exception = Assertions.assertThrows(SettingsException.class, () -> surveyUnitService
                .addSurveyUnitsToSample(emptyList, existingSampleId));
        Assertions.assertEquals("List of survey units to add is empty or null", exception.getMessage());
    }

    @Test
    void shouldReturnSettingsExceptionWhenAddNullListIntoSample() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        Long existingSampleId = importedSampleSurveyUnits.get(0).getSample().getId();

        // When + Then
        SettingsException exception = Assertions.assertThrows(SettingsException.class, () -> surveyUnitService
                .addSurveyUnitsToSample(null, existingSampleId));
        Assertions.assertEquals("List of survey units to add is empty or null", exception.getMessage());
    }

    @Test
    void shouldAddAListOfExistingSurveyUnitsIntoNewSampleAndReturnCountOfAdd() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        List<Long> existingSurveyUnits = importedSampleSurveyUnits.stream()
                .map(SampleSurveyUnitDto::getSurveyUnit)
                .map(SurveyUnitDto::getRepositoryId)
                .toList();
        SampleDto newSample = sampleService.createSample("new sample");
        Long newSampleId = newSample.getId();

        // When
        int count = surveyUnitService.addSurveyUnitsToSample(existingSurveyUnits, newSampleId);

        // Then
        Assertions.assertEquals(2, count);
        List<SampleSurveyUnitDto> sampleSurveyUnitDtoListAfterAdd =
                surveyUnitService.getSurveyUnitsBySampleId(newSampleId);
        Assertions.assertEquals(1L, sampleSurveyUnitDtoListAfterAdd.get(0).getSurveyUnit().getRepositoryId());
        Assertions.assertEquals(2L, sampleSurveyUnitDtoListAfterAdd.get(1).getSurveyUnit().getRepositoryId());
    }

    @Test
    void shouldReturnSurveyUnitsNotFoundExceptionWhenAddListWithNotExistingSurveyUnitIntoSample() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        List<Long> listWithNotExistingSurveyUnit =
                List.of(importedSampleSurveyUnits.get(0).getSurveyUnit().getRepositoryId(), 99L);
        Long existingSampleId = importedSampleSurveyUnits.get(0).getSample().getId();

        // When + Then
        SurveyUnitsNotFoundException exception = Assertions.assertThrows(SurveyUnitsNotFoundException.class, () ->
                surveyUnitService.addSurveyUnitsToSample(listWithNotExistingSurveyUnit, existingSampleId));
        Assertions.assertEquals(String.format("SurveyUnits [%s] don't exist", List.of(99L)), exception.getMessage());

    }

    @Test
    void shouldCheckSurveyUnitsIdsAndReturnTrueIfAtLeastOneSurveyUnitHasId() {
        // Given
        SurveyUnitDto su1 = SurveyUnitDto.builder().repositoryId(1L).build();
        SurveyUnitDto su2 = SurveyUnitDto.builder().repositoryId(2L).build();
        SurveyUnitDto su3 = SurveyUnitDto.builder().build();

        // When
        boolean check = surveyUnitService.checkRepositoryId(List.of(su1, su2, su3));

        // Then
        Assertions.assertTrue(check);
    }

    @Test
    void shouldCheckSurveyUnitsIdsAndReturnFalseIfAllSurveyUnitsHaveNotId() {
        // Given
        SurveyUnitDto su1 = SurveyUnitDto.builder().build();
        SurveyUnitDto su2 = SurveyUnitDto.builder().build();
        SurveyUnitDto su3 = SurveyUnitDto.builder().build();

        // When
        boolean check = surveyUnitService.checkRepositoryId(List.of(su1, su2, su3));

        // Then
        Assertions.assertFalse(check);
    }

    @Test
    void shouldCheckSurveyUnitsIdsAndReturnFalseIfListIsNull() {
        // When
        boolean check = surveyUnitService.checkRepositoryId(null);

        // Then
        Assertions.assertFalse(check);
    }

    @Test
    void shouldReturnSettingsExceptionWhenUpdateNullSurveyUnit() {
        // Given + When + Then
        SettingsException settingsException = Assertions.assertThrows(SettingsException.class, () -> surveyUnitService
                .updateSurveyUnit(null));
        Assertions.assertEquals("SurveyUnit data empty", settingsException.getMessage());
    }

    @Test
    void shouldReturnSettingsExceptionWhenUpdateSurveyUnitWithoutId() {
        // Given
        SurveyUnitDto suDto = SurveyUnitDto.builder().build();

        // When + Then
        SettingsException settingsException = Assertions.assertThrows(SettingsException.class, () -> surveyUnitService
                .updateSurveyUnit(suDto));
        Assertions.assertEquals("Repository id empty", settingsException.getMessage());
    }

    @Test
    void shouldReturnSurveyUnitNotFoundExceptionWhenUpdateNotExistingSurveyUnit() {
        // Given
        SurveyUnitDto notExistingSurveyUnit = SurveyUnitDto.builder().repositoryId(99L).build();

        // When + Then
        SurveyUnitNotFoundException exception =
                Assertions.assertThrows(SurveyUnitNotFoundException.class,
                        () -> surveyUnitService.updateSurveyUnit(notExistingSurveyUnit));
        Assertions.assertEquals(String.format("SurveyUnit [%s] doesn't exist",
                notExistingSurveyUnit.getRepositoryId()), exception.getMessage());

    }

    @Test
    void shouldUpdateExistingSurveyUnit() {
        // Given
        List<SampleSurveyUnitDto> importedSampleSurveyUnits =
                initDataPersistenceInMemoryWithOneSampleAndTwoSurveyUnits("init sample");
        SurveyUnitDto surveyUnitToUpdate = importedSampleSurveyUnits.get(0).getSurveyUnit();
        surveyUnitToUpdate.setExternalId("00003");
        surveyUnitToUpdate.setTypeUnit(TypeUnit.ENTERPRISE);

        // When
        SurveyUnitDto updatedSurveyUnit = surveyUnitService.updateSurveyUnit(surveyUnitToUpdate);

        // Then
        Assertions.assertNotNull(updatedSurveyUnit);
        Assertions.assertEquals(surveyUnitToUpdate.getRepositoryId(), updatedSurveyUnit.getRepositoryId());
        Assertions.assertEquals(surveyUnitToUpdate.getExternalId(), updatedSurveyUnit.getExternalId());
        Assertions.assertEquals(surveyUnitToUpdate.getTypeUnit(), updatedSurveyUnit.getTypeUnit());
    }

}
