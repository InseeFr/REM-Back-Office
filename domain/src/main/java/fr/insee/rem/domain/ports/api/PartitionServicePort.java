package fr.insee.rem.domain.ports.api;

import fr.insee.rem.domain.dtos.PartitionDto;

import java.util.List;

public interface PartitionServicePort {

    void deletePartitionById(Long partitionId);

    PartitionDto createPartition(String label);

    PartitionDto getPartitionById(Long partitionId);

    List<PartitionDto> getAllPartitions();

}
