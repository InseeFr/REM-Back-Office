package fr.insee.rem.domain.service.mock;

import fr.insee.rem.domain.dtos.PartitionDto;
import fr.insee.rem.domain.ports.spi.PartitionPersistencePort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PartitionPersistenceInMemory implements PartitionPersistencePort {

    private final List<PartitionDto> partitionsInMemory = new ArrayList<>();

    private Long sequence = 0L;

    @Override
    public void deleteById(Long partitionId) {
        partitionsInMemory.removeIf(partition -> partition.getPartitionId().equals(partitionId));
    }

    @Override
    public Optional<PartitionDto> findById(Long partitionId) {
        return partitionsInMemory.stream().filter(partition -> partition.getPartitionId().equals(partitionId)).findFirst();
    }

    @Override
    public List<PartitionDto> findAll() {
        return partitionsInMemory;
    }

    @Override
    public boolean existsById(Long partitionId) {
        return partitionsInMemory.stream().anyMatch(partition -> partition.getPartitionId().equals(partitionId));
    }

    @Override
    public boolean existsByLabel(String label) {
        return partitionsInMemory.stream().anyMatch(partition -> partition.getLabel().equals(label));
    }

    @Override
    public PartitionDto createPartition(String label) {
        PartitionDto partition = PartitionDto.builder().partitionId(++sequence).label(label).build();
        partitionsInMemory.add(partition);
        return partition;
    }
}
