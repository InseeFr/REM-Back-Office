package fr.insee.rem.domain.service;

import fr.insee.rem.domain.dtos.PartitionDto;
import fr.insee.rem.domain.exception.PartitionAlreadyExistsException;
import fr.insee.rem.domain.exception.PartitionNotFoundException;
import fr.insee.rem.domain.ports.api.PartitionServicePort;
import fr.insee.rem.domain.service.mock.PartitionPersistenceInMemory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


class PartitionServiceTest {

    PartitionPersistenceInMemory partitionPersistenceInMemory = new PartitionPersistenceInMemory();
    PartitionServicePort partitionService = new PartitionServiceImpl(partitionPersistenceInMemory);

    @Test
    void shouldCreatePartitionFromLabel() {
        // Given
        String label = "new label";

        // When
        PartitionDto partition = partitionService.createPartition(label);

        // Then
        Assertions.assertNotNull(partition);
        Assertions.assertEquals(label, partition.getLabel());
        Assertions.assertNotNull(partition.getPartitionId());
    }

    @Test
    void shouldReturnPartitionAlreadyExistsExceptionWhenCreateLabelAlreadyExists() {
        // Given
        String label = "existing label";
        partitionService.createPartition(label);

        // When + Then
        PartitionAlreadyExistsException exception = Assertions.assertThrows(PartitionAlreadyExistsException.class,
                () -> partitionService.createPartition(label));
        Assertions.assertEquals(String.format("Partition [%s] already exists", label), exception.getMessage());
    }

    @Test
    void shouldDeletePartitionFromId() {
        // Given
        PartitionDto partitionToDelete = partitionService.createPartition("partitionToDelete");
        Long partitionToDeleteId = partitionToDelete.getPartitionId();

        // When
        partitionService.deletePartitionById(partitionToDeleteId);

        // Then
        Assertions.assertFalse(partitionPersistenceInMemory.existsById(partitionToDeleteId));
    }

    @Test
    void shouldReturnPartitionNotFoundExceptionWhenDeleteNotExistingPartition() {
        // Given
        Long notExistingPartitionId = 99L;

        // When + Then
        PartitionNotFoundException exception = Assertions.assertThrows(PartitionNotFoundException.class,
                () -> partitionService.deletePartitionById(notExistingPartitionId));
        Assertions.assertEquals(String.format("Partition [%s] doesn't exist", notExistingPartitionId),
                exception.getMessage());
    }

    @Test
    void shouldReturnPartitionFromId() {
        // Given
        PartitionDto partitionToReturn = partitionService.createPartition("partitionToReturn");
        Long partitionToReturnId = partitionToReturn.getPartitionId();

        // When
        PartitionDto partitionReturned = partitionService.getPartitionById(partitionToReturnId);

        // Then
        Assertions.assertNotNull(partitionReturned);
        Assertions.assertEquals(partitionToReturn.getPartitionId(), partitionReturned.getPartitionId());
        Assertions.assertEquals(partitionToReturn.getLabel(), partitionReturned.getLabel());
    }

    @Test
    void shouldReturnPartitionNotFoundExceptionWhenGetNotExistingPartition() {
        // Given
        Long notExistingPartitionId = 99L;

        // When + Then
        PartitionNotFoundException exception = Assertions.assertThrows(PartitionNotFoundException.class,
                () -> partitionService.getPartitionById(notExistingPartitionId));
        Assertions.assertEquals(String.format("Partition [%s] doesn't exist", notExistingPartitionId),
                exception.getMessage());
    }

    @Test
    void shouldReturnListOfAllPartitions() {
        // Given
        PartitionDto partitionOne = partitionService.createPartition("partitionOne");
        PartitionDto partitionTwo = partitionService.createPartition("partitionTwo");

        // When
        List<PartitionDto> allPartitions = partitionService.getAllPartitions();

        // Then
        Assertions.assertFalse(allPartitions.isEmpty());
        Assertions.assertEquals(2, allPartitions.size());
        Assertions.assertEquals(partitionOne, allPartitions.get(0));
        Assertions.assertEquals(partitionTwo, allPartitions.get(1));
    }
}
