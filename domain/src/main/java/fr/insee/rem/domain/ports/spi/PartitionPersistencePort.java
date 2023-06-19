package fr.insee.rem.domain.ports.spi;

import fr.insee.rem.domain.dtos.PartitionDto;

import java.util.List;
import java.util.Optional;

public interface PartitionPersistencePort {

    void deleteById(Long partitionId);

    Optional<PartitionDto> findById(Long partitionId);

    List<PartitionDto> findAll();

    boolean existsById(Long partitionId);

    boolean existsByLabel(String label);

    PartitionDto createPartition(String label);

}
