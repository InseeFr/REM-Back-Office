package fr.insee.rem.domain.service.mock;

import fr.insee.rem.domain.dtos.SampleDto;
import fr.insee.rem.domain.ports.spi.SamplePersistencePort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SamplePersistenceInMemory implements SamplePersistencePort {

    private final List<SampleDto> samplesInMemory = new ArrayList<>();

    private Long sequence = 0L;

    @Override
    public void deleteById(Long sampleId) {
        samplesInMemory.removeIf(sample -> sample.getId().equals(sampleId));
    }

    @Override
    public Optional<SampleDto> findById(Long sampleId) {
        return samplesInMemory.stream().filter(sample -> sample.getId().equals(sampleId)).findFirst();
    }

    @Override
    public List<SampleDto> findAll() {
        return samplesInMemory;
    }

    @Override
    public boolean existsById(Long sampleId) {
        return samplesInMemory.stream().anyMatch(sample -> sample.getId().equals(sampleId));
    }

    @Override
    public boolean existsByLabel(String label) {
        return samplesInMemory.stream().anyMatch(sample -> sample.getLabel().equals(label));
    }

    @Override
    public SampleDto createSample(String label) {
        SampleDto newSample = SampleDto.builder().id(++sequence).label(label).build();
        samplesInMemory.add(newSample) ;
        return newSample;
    }
}
