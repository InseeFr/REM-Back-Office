package fr.insee.rem.domain.service;

import fr.insee.rem.domain.dtos.Context;
import fr.insee.rem.domain.dtos.PartitionDto;
import fr.insee.rem.domain.dtos.PartitionSurveyUnitLinkDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.exception.PartitionNotFoundException;
import fr.insee.rem.domain.exception.SettingsException;
import fr.insee.rem.domain.exception.SurveyUnitNotFoundException;
import fr.insee.rem.domain.exception.SurveyUnitsNotFoundException;
import fr.insee.rem.domain.ports.api.PartitionServicePort;
import fr.insee.rem.domain.ports.api.SurveyUnitServicePort;
import fr.insee.rem.domain.records.SuIdMappingRecord;
import fr.insee.rem.domain.service.mock.PartitionPersistenceInMemory;
import fr.insee.rem.domain.service.mock.PartitionSurveyUnitLinkPersistenceInMemory;
import fr.insee.rem.domain.service.mock.SurveyUnitPersistenceInMemory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class SurveyUnitServiceTest {
    PartitionPersistenceInMemory partitionPersistenceInMemory = new PartitionPersistenceInMemory();
    SurveyUnitPersistenceInMemory surveyUnitPersistenceInMemory = new SurveyUnitPersistenceInMemory();
    PartitionSurveyUnitLinkPersistenceInMemory partitionSurveyUnitLinkPersistenceInMemory =
            new PartitionSurveyUnitLinkPersistenceInMemory(partitionPersistenceInMemory, surveyUnitPersistenceInMemory);

    SurveyUnitServicePort surveyUnitService = new SurveyUnitServiceImpl(surveyUnitPersistenceInMemory,
            partitionSurveyUnitLinkPersistenceInMemory, partitionPersistenceInMemory);
    PartitionServicePort partitionService = new PartitionServiceImpl(partitionPersistenceInMemory);

    private List<PartitionSurveyUnitLinkDto> initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits(String label) {
        PartitionDto existingPartition = partitionService.createPartition(label);
        List<SurveyUnitDto> surveyUnitsToImport =
                List.of(SurveyUnitDto.builder().context(Context.HOUSEHOLD).externalId("00001").build(),
                        SurveyUnitDto.builder().context(Context.HOUSEHOLD).externalId("00002").build());
        return surveyUnitService.importSurveyUnitsIntoPartition(existingPartition.getPartitionId(),
                surveyUnitsToImport);
    }

    @Test
    void shouldImportListOfSurveyUnitsAndLinkThemToExistingPartition() {
        // Given
        PartitionDto existingPartition = partitionService.createPartition("existing partition");
        List<SurveyUnitDto> surveyUnitsToImport =
                List.of(SurveyUnitDto.builder().context(Context.HOUSEHOLD).externalId("00001").build(),
                        SurveyUnitDto.builder().context(Context.HOUSEHOLD).externalId("00002").build());

        // When
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                surveyUnitService.importSurveyUnitsIntoPartition(existingPartition.getPartitionId(),
                        surveyUnitsToImport);

        // Then
        Assertions.assertNotNull(importedPartitionSurveyUnits);
        Assertions.assertEquals(2, importedPartitionSurveyUnits.size());
        Assertions.assertEquals(1L, importedPartitionSurveyUnits.get(0).getSurveyUnit().getRepositoryId());
        Assertions.assertEquals(2L, importedPartitionSurveyUnits.get(1).getSurveyUnit().getRepositoryId());
        Assertions.assertEquals("00001", importedPartitionSurveyUnits.get(0).getSurveyUnit().getExternalId());
        Assertions.assertEquals("00002", importedPartitionSurveyUnits.get(1).getSurveyUnit().getExternalId());
        Assertions.assertEquals(1L, importedPartitionSurveyUnits.get(0).getPartition().getPartitionId());
        Assertions.assertEquals(1L, importedPartitionSurveyUnits.get(1).getPartition().getPartitionId());
    }

    @Test
    void shouldReturnPartitionNotFoundExceptionWhenImportSurveyUnitsIntoNotExistingPartition() {
        // Given
        List<SurveyUnitDto> surveyUnitsToImport =
                List.of(SurveyUnitDto.builder().context(Context.HOUSEHOLD).externalId("00001").build(),
                        SurveyUnitDto.builder().context(Context.HOUSEHOLD).externalId("00002").build());
        Long notExistingPartitionId = 99L;

        // When + Then
        PartitionNotFoundException exception =
                Assertions.assertThrows(PartitionNotFoundException.class,
                        () -> surveyUnitService.importSurveyUnitsIntoPartition(notExistingPartitionId,
                                surveyUnitsToImport));
        Assertions.assertEquals(String.format("Partition [%s] doesn't exist", notExistingPartitionId),
                exception.getMessage());

    }

    @Test
    void shouldReturnSettingsExceptionWhenImportNullListToExistingPartition() {
        // Given
        Long existingPartitionId = 1L;

        // When + Then
        SettingsException exception = Assertions.assertThrows(SettingsException.class,
                () -> surveyUnitService.importSurveyUnitsIntoPartition(existingPartitionId, null));
        Assertions.assertEquals("List of survey units to import is empty or null", exception.getMessage());
    }

    @Test
    void shouldReturnSettingsExceptionWhenImportEmptyListToExistingPartition() {
        // Given
        List<SurveyUnitDto> emptyList = List.of();
        Long existingPartitionId = 1L;

        // When + Then
        SettingsException exception = Assertions.assertThrows(SettingsException.class,
                () -> surveyUnitService.importSurveyUnitsIntoPartition(existingPartitionId, emptyList));
        Assertions.assertEquals("List of survey units to import is empty or null", exception.getMessage());
    }

    @Test
    void shouldAddAnExistingSurveyUnitIntoAnotherPartition() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        Long existingSurveyUnitId = importedPartitionSurveyUnits.get(0).getSurveyUnit().getRepositoryId();
        PartitionDto anotherPartition = partitionService.createPartition("new partition");
        Long anotherPartitionId = anotherPartition.getPartitionId();

        // When
        PartitionSurveyUnitLinkDto addedPartitionSurveyUnit =
                surveyUnitService.addExistingSurveyUnitIntoPartition(existingSurveyUnitId,
                        anotherPartitionId);

        // Then
        Assertions.assertNotNull(addedPartitionSurveyUnit);
        Assertions.assertEquals(existingSurveyUnitId, addedPartitionSurveyUnit.getSurveyUnit().getRepositoryId());
        Assertions.assertEquals(anotherPartitionId, addedPartitionSurveyUnit.getPartition().getPartitionId());
    }

    @Test
    void shouldReturnPartitionNotFoundExceptionWhenAddExistingSurveyUnitIntoNonExistentPartition() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        Long existingSurveyUnitId = importedPartitionSurveyUnits.get(0).getSurveyUnit().getRepositoryId();
        Long notExistingPartitionId = 99L;

        // When + Then
        PartitionNotFoundException exception = Assertions.assertThrows(PartitionNotFoundException.class,
                () -> surveyUnitService.addExistingSurveyUnitIntoPartition(existingSurveyUnitId,
                        notExistingPartitionId));
        Assertions.assertEquals(String.format("Partition [%s] doesn't exist", notExistingPartitionId),
                exception.getMessage());
    }

    @Test
    void shouldReturnSurveyUnitNotFoundExceptionWhenAddNotExistingSurveyUnitIntoExistingPartition() {
        // Given
        PartitionDto existingPartition = partitionService.createPartition("existing partition");
        Long existingPartitionId = existingPartition.getPartitionId();
        Long notExistingSurveyUnitId = 99L;

        // When + Then
        SurveyUnitNotFoundException exception = Assertions.assertThrows(SurveyUnitNotFoundException.class,
                () -> surveyUnitService.addExistingSurveyUnitIntoPartition(notExistingSurveyUnitId,
                        existingPartitionId));
        Assertions.assertEquals(String.format("SurveyUnit [%s] doesn't exist", notExistingSurveyUnitId),
                exception.getMessage());
    }

    @Test
    void shouldDeleteSurveyUnit() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        Long surveyUnitIdToDelete = importedPartitionSurveyUnits.get(0).getSurveyUnit().getRepositoryId();

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
    void shouldRemoveSurveyUnitFromPartition() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        Long surveyUnitIdToRemove = importedPartitionSurveyUnits.get(0).getSurveyUnit().getRepositoryId();
        Long existingPartitionId = importedPartitionSurveyUnits.get(0).getPartition().getPartitionId();

        // When
        surveyUnitService.removeSurveyUnitFromPartition(surveyUnitIdToRemove, existingPartitionId);

        // Then
        Assertions.assertEquals(1,
                partitionSurveyUnitLinkPersistenceInMemory.findSurveyUnitsByPartitionId(existingPartitionId).size());
    }

    @Test
    void shouldReturnPartitionNotFoundExceptionWhenRemoveExistingSurveyUnitFromNotExistingPartition() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        Long existingSurveyUnitId = importedPartitionSurveyUnits.get(0).getSurveyUnit().getRepositoryId();
        Long notExistingPartitionId = 99L;

        // When + Then
        PartitionNotFoundException exception = Assertions.assertThrows(PartitionNotFoundException.class,
                () -> surveyUnitService.removeSurveyUnitFromPartition(existingSurveyUnitId, notExistingPartitionId));
        Assertions.assertEquals(String.format("Partition [%s] doesn't exist", notExistingPartitionId),
                exception.getMessage());
    }

    @Test
    void shouldReturnSurveyUnitNotFoundExceptionWhenRemoveNotExistingSurveyUnitFromExistingPartition() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        Long existingPartitionId = importedPartitionSurveyUnits.get(0).getPartition().getPartitionId();
        Long notExistingSurveyUnitId = 99L;

        // When + Then
        SurveyUnitNotFoundException exception = Assertions.assertThrows(SurveyUnitNotFoundException.class,
                () -> surveyUnitService.removeSurveyUnitFromPartition(notExistingSurveyUnitId, existingPartitionId));
        Assertions.assertEquals(String.format("SurveyUnit [%s] doesn't exist", notExistingSurveyUnitId),
                exception.getMessage());
    }

    @Test
    void shouldReturnSurveyUnitFromId() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        Long existingSurveyUnitId = importedPartitionSurveyUnits.get(0).getSurveyUnit().getRepositoryId();

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
    void shouldReturnPartitionNotFoundExceptionWhenGetListOfSurveyUnitsFromNotExistingPartition() {
        // Given
        Long notExistingPartitionId = 99L;

        // When + Then
        PartitionNotFoundException exception = Assertions.assertThrows(PartitionNotFoundException.class,
                () -> surveyUnitService.getSurveyUnitsByPartitionId(notExistingPartitionId));
        Assertions.assertEquals(String.format("Partition [%s] doesn't exist", notExistingPartitionId),
                exception.getMessage());
    }


    @Test
    void shouldReturnListOfSurveyUnitsLinkToExistingPartition() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        List<PartitionSurveyUnitLinkDto> otherImportedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("other partition");
        List<Long> idsNotInRetournedList = otherImportedPartitionSurveyUnits.stream()
                .map(PartitionSurveyUnitLinkDto::getSurveyUnit)
                .map(SurveyUnitDto::getRepositoryId)
                .toList();
        Long importPartitionId = importedPartitionSurveyUnits.get(0).getPartition().getPartitionId();

        // When
        List<PartitionSurveyUnitLinkDto> returnedList =
                surveyUnitService.getSurveyUnitsByPartitionId(importPartitionId);

        // Then
        Assertions.assertNotNull(returnedList);
        Assertions.assertEquals(importedPartitionSurveyUnits.size(), returnedList.size());
        for (int i = 0; i < returnedList.size(); i++) {
            Assertions.assertEquals(importedPartitionSurveyUnits.get(i).getSurveyUnit().getRepositoryId(),
                    returnedList.get(i).getSurveyUnit().getRepositoryId());
            Assertions.assertEquals(importedPartitionSurveyUnits.get(i).getPartition().getPartitionId(),
                    returnedList.get(i).getPartition().getPartitionId());
            Assertions.assertFalse(idsNotInRetournedList.contains(returnedList.get(i).getSurveyUnit().getRepositoryId()));
        }
    }

    @Test
    void shouldReturnPartitionNotFoundExceptionWhenGetListOfSurveyUnitIdsFromNotExistingPartition() {
        // Given
        Long notExistingPartitionId = 99L;

        // When + Then
        PartitionNotFoundException exception = Assertions.assertThrows(PartitionNotFoundException.class,
                () -> surveyUnitService.getSurveyUnitIdsByPartitionId(notExistingPartitionId));
        Assertions.assertEquals(String.format("Partition [%s] doesn't exist", notExistingPartitionId),
                exception.getMessage());
    }


    @Test
    void shouldReturnListOfSurveyUnitIdsLinkToExistingPartition() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        List<PartitionSurveyUnitLinkDto> otherImportedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("other partition");
        List<Long> idsNotInRetournedList = otherImportedPartitionSurveyUnits.stream()
                .map(PartitionSurveyUnitLinkDto::getSurveyUnit)
                .map(SurveyUnitDto::getRepositoryId)
                .toList();
        Long importPartitionId = importedPartitionSurveyUnits.get(0).getPartition().getPartitionId();

        // When
        List<Long> returnedIds = surveyUnitService.getSurveyUnitIdsByPartitionId(importPartitionId);

        // Then
        Assertions.assertNotNull(returnedIds);
        Assertions.assertEquals(importedPartitionSurveyUnits.size(), returnedIds.size());
        for (int i = 0; i < returnedIds.size(); i++) {
            Assertions.assertEquals(importedPartitionSurveyUnits.get(i).getSurveyUnit().getRepositoryId(),
                    returnedIds.get(i));
            Assertions.assertFalse(idsNotInRetournedList.contains(returnedIds.get(i)));
        }
    }

    @Test
    void shouldReturnPartitionNotFoundExceptionWhenGetListOfSurveyUnitIdsMappingTableFromNotExistingPartition() {
        // Given
        Long notExistingPartitionId = 99L;

        // When + Then
        PartitionNotFoundException exception = Assertions.assertThrows(PartitionNotFoundException.class,
                () -> surveyUnitService.getSurveyUnitIdsMappingTableByPartitionId(notExistingPartitionId));
        Assertions.assertEquals(String.format("Partition [%s] doesn't exist", notExistingPartitionId),
                exception.getMessage());
    }

    @Test
    void shouldReturnSurveyUnitIdsMappingTableLinkToExistingPartition() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        List<PartitionSurveyUnitLinkDto> otherImportedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("other partition");
        List<Long> idsNotInRetournedList = otherImportedPartitionSurveyUnits.stream()
                .map(PartitionSurveyUnitLinkDto::getSurveyUnit)
                .map(SurveyUnitDto::getRepositoryId)
                .toList();
        Long importPartitionId = importedPartitionSurveyUnits.get(0).getPartition().getPartitionId();

        // When
        List<SuIdMappingRecord> suIdsMapping =
                surveyUnitService.getSurveyUnitIdsMappingTableByPartitionId(importPartitionId);

        // Then
        Assertions.assertNotNull(suIdsMapping);
        Assertions.assertEquals(importedPartitionSurveyUnits.size(), suIdsMapping.size());
        for (int i = 0; i < suIdsMapping.size(); i++) {
            Assertions.assertEquals(importedPartitionSurveyUnits.get(i).getSurveyUnit().getRepositoryId(),
                    suIdsMapping.get(i).repositoryId());
            Assertions.assertEquals(importedPartitionSurveyUnits.get(i).getSurveyUnit().getExternalId(),
                    suIdsMapping.get(i).externalId());
            Assertions.assertFalse(idsNotInRetournedList.contains(suIdsMapping.get(i).repositoryId()));
        }


    }

    @Test
    void shouldReturnPartitionNotFoundExceptionWhenAddListOfSurveyUnitsIntoNotExistingPartition() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        List<Long> existingSurveyUnits = importedPartitionSurveyUnits.stream()
                .map(PartitionSurveyUnitLinkDto::getSurveyUnit)
                .map(SurveyUnitDto::getRepositoryId)
                .toList();
        Long notExistingPartitionId = 99L;

        // When + Then
        PartitionNotFoundException exception = Assertions.assertThrows(PartitionNotFoundException.class,
                () -> surveyUnitService.addExistingSurveyUnitsToPartition(existingSurveyUnits, notExistingPartitionId));
        Assertions.assertEquals(String.format("Partition [%s] doesn't exist", notExistingPartitionId),
                exception.getMessage());
    }

    @Test
    void shouldReturnSettingsExceptionWhenAddEmptyListIntoExistingPartition() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        Long existingPartitionId = importedPartitionSurveyUnits.get(0).getPartition().getPartitionId();
        List<Long> emptyList = List.of();

        // When + Then
        SettingsException exception = Assertions.assertThrows(SettingsException.class, () -> surveyUnitService
                .addExistingSurveyUnitsToPartition(emptyList, existingPartitionId));
        Assertions.assertEquals("List of survey units to add is empty or null", exception.getMessage());
    }

    @Test
    void shouldReturnSettingsExceptionWhenAddNullListIntoPartition() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        Long existingPartitionId = importedPartitionSurveyUnits.get(0).getPartition().getPartitionId();

        // When + Then
        SettingsException exception = Assertions.assertThrows(SettingsException.class, () -> surveyUnitService
                .addExistingSurveyUnitsToPartition(null, existingPartitionId));
        Assertions.assertEquals("List of survey units to add is empty or null", exception.getMessage());
    }

    @Test
    void shouldAddAListOfExistingSurveyUnitsIntoNewPartitionAndReturnCountOfAdd() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        List<Long> existingSurveyUnits = importedPartitionSurveyUnits.stream()
                .map(PartitionSurveyUnitLinkDto::getSurveyUnit)
                .map(SurveyUnitDto::getRepositoryId)
                .toList();
        PartitionDto newPartition = partitionService.createPartition("new partition");
        Long newPartitionId = newPartition.getPartitionId();

        // When
        int count = surveyUnitService.addExistingSurveyUnitsToPartition(existingSurveyUnits, newPartitionId);

        // Then
        Assertions.assertEquals(2, count);
        List<PartitionSurveyUnitLinkDto> partitionSurveyUnitLinkListAfterAdd =
                surveyUnitService.getSurveyUnitsByPartitionId(newPartitionId);
        Assertions.assertEquals(1L, partitionSurveyUnitLinkListAfterAdd.get(0).getSurveyUnit().getRepositoryId());
        Assertions.assertEquals(2L, partitionSurveyUnitLinkListAfterAdd.get(1).getSurveyUnit().getRepositoryId());
    }

    @Test
    void shouldReturnSurveyUnitsNotFoundExceptionWhenAddListWithNotExistingSurveyUnitIntoPartition() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        List<Long> listWithNotExistingSurveyUnit =
                List.of(importedPartitionSurveyUnits.get(0).getSurveyUnit().getRepositoryId(), 99L);
        Long existingPartitionId = importedPartitionSurveyUnits.get(0).getPartition().getPartitionId();

        // When + Then
        SurveyUnitsNotFoundException exception = Assertions.assertThrows(SurveyUnitsNotFoundException.class, () ->
                surveyUnitService.addExistingSurveyUnitsToPartition(listWithNotExistingSurveyUnit,
                        existingPartitionId));
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
        SurveyUnitDto surveyUnitWithoutId = SurveyUnitDto.builder().build();

        // When + Then
        SettingsException settingsException = Assertions.assertThrows(SettingsException.class, () -> surveyUnitService
                .updateSurveyUnit(surveyUnitWithoutId));
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
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        SurveyUnitDto surveyUnitToUpdate = importedPartitionSurveyUnits.get(0).getSurveyUnit();
        surveyUnitToUpdate.setExternalId("00003");
        surveyUnitToUpdate.setContext(Context.BUSINESS);

        // When
        SurveyUnitDto updatedSurveyUnit = surveyUnitService.updateSurveyUnit(surveyUnitToUpdate);

        // Then
        Assertions.assertNotNull(updatedSurveyUnit);
        Assertions.assertEquals(surveyUnitToUpdate.getRepositoryId(), updatedSurveyUnit.getRepositoryId());
        Assertions.assertEquals(surveyUnitToUpdate.getExternalId(), updatedSurveyUnit.getExternalId());
        Assertions.assertEquals(surveyUnitToUpdate.getContext(), updatedSurveyUnit.getContext());
    }

    @Test
    void shouldReturn0WhenPartitionHaveNoSurveyUnit() {
        // Given
        PartitionDto partition = partitionService.createPartition("partition");
        Long partitionId = partition.getPartitionId();

        // When
        long count = surveyUnitService.countSurveyUnitsByPartition(partitionId);

        // Then
        Assertions.assertEquals(0, count);
    }

    @Test
    void shouldReturnNumberOfSurveyUnitWhenPartitionHaveSurveyUnit() {
        // Given
        List<PartitionSurveyUnitLinkDto> importedPartitionSurveyUnits =
                initDataPersistenceInMemoryWithOnePartitionAndTwoSurveyUnits("init partition");
        Long partitionId = importedPartitionSurveyUnits.get(0).getPartition().getPartitionId();

        // When
        long count = surveyUnitService.countSurveyUnitsByPartition(partitionId);

        // Then
        Assertions.assertEquals(2, count);
    }

    @Test
    void shouldReturnPartitionNotFoundExceptionWhenCountSurveyUnitByNotExistingPartition() {
        // Given
        Long notExistingPartitionId = 99L;

        // When + Then
        PartitionNotFoundException exception =
                Assertions.assertThrows(PartitionNotFoundException.class,
                        () -> surveyUnitService.countSurveyUnitsByPartition(notExistingPartitionId));
        Assertions.assertEquals(String.format("Partition [%s] doesn't exist", notExistingPartitionId),
                exception.getMessage());
    }

}
