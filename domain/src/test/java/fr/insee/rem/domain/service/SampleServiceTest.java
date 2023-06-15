package fr.insee.rem.domain.service;

import fr.insee.rem.domain.dtos.SampleDto;
import fr.insee.rem.domain.exception.SampleAlreadyExistsException;
import fr.insee.rem.domain.exception.SampleNotFoundException;
import fr.insee.rem.domain.ports.api.SampleServicePort;
import fr.insee.rem.domain.service.mock.SamplePersistenceInMemory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


class SampleServiceTest {

    SamplePersistenceInMemory samplePersistenceInMemory = new SamplePersistenceInMemory();
    SampleServicePort sampleService = new SampleServiceImpl(samplePersistenceInMemory);

    @Test
    void shouldCreateSampleFromLabel() {
        // Given
        String label = "new label";

        // When
        SampleDto sampleDto = sampleService.createSample(label);

        // Then
        Assertions.assertNotNull(sampleDto);
        Assertions.assertEquals(label, sampleDto.getLabel());
        Assertions.assertNotNull(sampleDto.getId());
    }

    @Test
    void shouldReturnSampleAlreadyExistsExceptionWhenCreateLabelAlreadyExists() {
        // Given
        String label = "existing label";
        sampleService.createSample(label);

        // When + Then
        SampleAlreadyExistsException exception = Assertions.assertThrows(SampleAlreadyExistsException.class,
                () -> sampleService.createSample(label));
        Assertions.assertEquals(String.format("Sample [%s] already exists", label), exception.getMessage());
    }

    @Test
    void shouldDeleteSampleFromId() {
        // Given
        SampleDto sampleToDelete = sampleService.createSample("sampleToDelete");
        Long sampleToDeleteId = sampleToDelete.getId();

        // When
        sampleService.deleteSampleById(sampleToDeleteId);

        // Then
        Assertions.assertFalse(samplePersistenceInMemory.existsById(sampleToDeleteId));
    }

    @Test
    void shouldReturnSampleNotFoundExceptionWhenDeleteNotExistingSample() {
        // Given
        Long notExistingSampleId = 99L;

        // When + Then
        SampleNotFoundException exception = Assertions.assertThrows(SampleNotFoundException.class,
                () -> sampleService.deleteSampleById(notExistingSampleId));
        Assertions.assertEquals(String.format("Sample [%s] doesn't exist", notExistingSampleId),
                exception.getMessage());
    }

    @Test
    void shouldReturnSampleFromId() {
        // Given
        SampleDto sampleToReturn = sampleService.createSample("sampleToReturn");
        Long sampleToReturnId = sampleToReturn.getId();

        // When
        SampleDto sampleReturned = sampleService.getSampleById(sampleToReturnId);

        // Then
        Assertions.assertNotNull(sampleReturned);
        Assertions.assertEquals(sampleToReturn.getId(), sampleReturned.getId());
        Assertions.assertEquals(sampleToReturn.getLabel(), sampleReturned.getLabel());
    }

    @Test
    void shouldReturnSampleNotFoundExceptionWhenGetNotExistingSample() {
        // Given
        Long notExistingSampleId = 99L;

        // When + Then
        SampleNotFoundException exception = Assertions.assertThrows(SampleNotFoundException.class,
                () -> sampleService.getSampleById(notExistingSampleId));
        Assertions.assertEquals(String.format("Sample [%s] doesn't exist", notExistingSampleId),
                exception.getMessage());
    }

    @Test
    void shouldReturnListOfAllSamples() {
        // Given
        SampleDto sampleOne = sampleService.createSample("sampleOne");
        SampleDto sampleTwo = sampleService.createSample("sampleTwo");

        // When
        List<SampleDto> allSamples = sampleService.getAllSamples();

        // Then
        Assertions.assertFalse(allSamples.isEmpty());
        Assertions.assertEquals(2, allSamples.size());
        Assertions.assertEquals(sampleOne, allSamples.get(0));
        Assertions.assertEquals(sampleTwo, allSamples.get(1));
    }
}
