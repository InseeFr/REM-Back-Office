package fr.insee.rem.domain.ports.spi;

import java.util.List;
import java.util.Optional;

import fr.insee.rem.domain.dtos.SampleDto;

public interface SamplePersistencePort {

    void deleteById(Long sampleId);

    Optional<SampleDto> findById(Long sampleId);

    List<SampleDto> findAll();

    Optional<SampleDto> findByLabel(String label);

    SampleDto save(SampleDto sampleDto);

    boolean existsById(Long sampleId);

}
