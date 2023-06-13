package fr.insee.rem.domain.ports.spi;

import java.util.List;
import java.util.Optional;

import fr.insee.rem.domain.dtos.SampleDto;

public interface SamplePersistencePort {

    void deleteById(Long sampleId);

    Optional<SampleDto> findById(Long sampleId);

    List<SampleDto> findAll();

    boolean existsById(Long sampleId);

    boolean existsByLabel(String label);

    SampleDto createSample(String label);

}
