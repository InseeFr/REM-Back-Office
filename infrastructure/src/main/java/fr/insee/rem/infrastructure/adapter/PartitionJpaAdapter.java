package fr.insee.rem.infrastructure.adapter;

import fr.insee.rem.domain.dtos.PartitionDto;
import fr.insee.rem.domain.ports.spi.PartitionPersistencePort;
import fr.insee.rem.infrastructure.entity.PartitionEntity;
import fr.insee.rem.infrastructure.mappers.PartitionMapper;
import fr.insee.rem.infrastructure.repository.PartitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PartitionJpaAdapter implements PartitionPersistencePort {

    @Autowired
    private PartitionRepository partitionRepository;

    @Override
    public void deleteById(Long partitionId) {
        partitionRepository.deleteById(partitionId);
    }

    @Override
    public boolean existsById(Long partitionId) {
        return partitionRepository.existsById(partitionId);
    }

    @Override
    public Optional<PartitionDto> findById(Long partitionId) {
        Optional<PartitionEntity> partition = partitionRepository.findById(partitionId);
        if (partition.isPresent()) {
            return Optional.of(PartitionMapper.INSTANCE.entityToDto(partition.get()));
        }
        return Optional.empty();

    }

    @Override
    public List<PartitionDto> findAll() {
        List<PartitionEntity> partitionEntities = partitionRepository.findAll();
        return PartitionMapper.INSTANCE.listEntityToListDto(partitionEntities);
    }

    @Override
    public boolean existsByLabel(String label) {
        return partitionRepository.findByLabel(label).isPresent();
    }

    @Override
    public PartitionDto createPartition(String label) {
        PartitionEntity partitionEntity = new PartitionEntity();
        partitionEntity.setLabel(label);
        partitionEntity = partitionRepository.save(partitionEntity);
        return PartitionMapper.INSTANCE.entityToDto(partitionEntity);
    }

}
