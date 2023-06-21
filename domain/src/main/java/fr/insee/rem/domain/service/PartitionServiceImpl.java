package fr.insee.rem.domain.service;

import fr.insee.rem.domain.dtos.PartitionDto;
import fr.insee.rem.domain.exception.PartitionAlreadyExistsException;
import fr.insee.rem.domain.exception.PartitionNotFoundException;
import fr.insee.rem.domain.ports.api.PartitionServicePort;
import fr.insee.rem.domain.ports.spi.PartitionPersistencePort;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class PartitionServiceImpl implements PartitionServicePort {

    private PartitionPersistencePort partitionPersistencePort;

    public PartitionServiceImpl(PartitionPersistencePort partitionPersistencePort) {
        this.partitionPersistencePort = partitionPersistencePort;
    }

    @Override
    public void deletePartitionById(Long partitionId) {
        log.debug("domain: deletePartitionById({})", partitionId);
        if (!partitionPersistencePort.existsById(partitionId)) {
            throw new PartitionNotFoundException(partitionId);
        }
        partitionPersistencePort.deleteById(partitionId);
    }

    @Override
    public PartitionDto createPartition(String label) {
        log.debug("domain: createPartition({})", label);
        if (partitionPersistencePort.existsByLabel(label)) {
            throw new PartitionAlreadyExistsException(label);
        }
        return partitionPersistencePort.createPartition(label);
    }

    @Override
    public PartitionDto getPartitionById(Long partitionId) {
        log.debug("domain: getPartitionById({})", partitionId);
        Optional<PartitionDto> findPartition = partitionPersistencePort.findById(partitionId);
        if (!findPartition.isPresent()) {
            throw new PartitionNotFoundException(partitionId);
        }
        return findPartition.get();
    }

    @Override
    public List<PartitionDto> getAllPartitions() {
        log.debug("domain: getAllPartitions()");
        return partitionPersistencePort.findAll();
    }

}
