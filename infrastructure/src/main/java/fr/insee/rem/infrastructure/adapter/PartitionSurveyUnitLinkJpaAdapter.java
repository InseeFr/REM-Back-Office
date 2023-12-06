package fr.insee.rem.infrastructure.adapter;

import fr.insee.rem.domain.dtos.PartitionSurveyUnitLinkDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.ports.spi.PartitionSurveyUnitLinkPersistencePort;
import fr.insee.rem.domain.records.SuIdMappingRecord;
import fr.insee.rem.infrastructure.entity.PartitionEntity;
import fr.insee.rem.infrastructure.entity.PartitionSurveyUnitLinkEntity;
import fr.insee.rem.infrastructure.entity.SurveyUnitEntity;
import fr.insee.rem.infrastructure.mappers.PartitionSurveyUnitLinkMapper;
import fr.insee.rem.infrastructure.mappers.SurveyUnitMapper;
import fr.insee.rem.infrastructure.repository.PartitionRepository;
import fr.insee.rem.infrastructure.repository.PartitionSurveyUnitLinkRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PartitionSurveyUnitLinkJpaAdapter implements PartitionSurveyUnitLinkPersistencePort {

    @Autowired
    private PartitionSurveyUnitLinkRepository partitionSurveyUnitLinkRepository;

    @Autowired
    private PartitionRepository partitionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 100;

    @Override
    public List<PartitionSurveyUnitLinkDto> saveAll(Long partitionId, List<SurveyUnitDto> surveyUnits) {
        Optional<PartitionEntity> optionalPartition = partitionRepository.findById(partitionId);
        List<SurveyUnitEntity> surveyUnitEntities = SurveyUnitMapper.INSTANCE.listDtoToListEntity(surveyUnits);
        PartitionEntity partitionEntity = optionalPartition.orElseThrow();
        List<PartitionSurveyUnitLinkEntity> partitionSurveyUnitLinkEntities = new ArrayList<>();
        for (int i = 0; i < surveyUnitEntities.size(); i++) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
                partitionEntity = entityManager.merge(partitionEntity); // re-attach entity to the persistence context
            }
            SurveyUnitEntity su = surveyUnitEntities.get(i);
            entityManager.persist(su);
            PartitionSurveyUnitLinkEntity link = new PartitionSurveyUnitLinkEntity(partitionEntity, su);
            entityManager.persist(link);
            partitionSurveyUnitLinkEntities.add(link);
        }
        return PartitionSurveyUnitLinkMapper.INSTANCE.listEntityToListDto(partitionSurveyUnitLinkEntities);
    }

    @Override
    public List<Long> findAllIdsByPartitionId(Long partitionId) {
        return partitionSurveyUnitLinkRepository.findAllIdsByPartitionId(partitionId);
    }

    @Override
    public PartitionSurveyUnitLinkDto addExistingSurveyUnitIntoPartition(Long surveyUnitId, Long partitionId) {
        PartitionEntity partitionEntity = entityManager.find(PartitionEntity.class, partitionId);
        SurveyUnitEntity surveyUnitEntity = entityManager.find(SurveyUnitEntity.class, surveyUnitId);
        PartitionSurveyUnitLinkEntity link = new PartitionSurveyUnitLinkEntity(partitionEntity, surveyUnitEntity);
        link = partitionSurveyUnitLinkRepository.save(link);
        return PartitionSurveyUnitLinkMapper.INSTANCE.entityToDto(link);
    }

    @Override
    public void removeSurveyUnitFromPartition(Long surveyUnitId, Long partitionId) {
        PartitionEntity partitionEntity = entityManager.find(PartitionEntity.class, partitionId);
        SurveyUnitEntity surveyUnitEntity = entityManager.find(SurveyUnitEntity.class, surveyUnitId);
        PartitionSurveyUnitLinkEntity link = new PartitionSurveyUnitLinkEntity(partitionEntity, surveyUnitEntity);
        partitionSurveyUnitLinkRepository.delete(link);
    }

    @Override
    public List<PartitionSurveyUnitLinkDto> findSurveyUnitsByPartitionId(Long partitionId) {
        List<PartitionSurveyUnitLinkEntity> links =
                partitionSurveyUnitLinkRepository.findAllSurveyUnitsByPartitionId(partitionId);
        return PartitionSurveyUnitLinkMapper.INSTANCE.listEntityToListDto(links);
    }

    @Override
    public List<SuIdMappingRecord> findSurveyUnitIdsMappingTableByPartitionId(Long partitionId) {
        return partitionSurveyUnitLinkRepository.findSuIdMappingByPartitionId(partitionId);
    }

    @Override
    public long countByPartitionId(Long partitionId) {
        PartitionEntity partitionEntity = entityManager.find(PartitionEntity.class, partitionId);
        return partitionSurveyUnitLinkRepository.countByPartition(partitionEntity);
    }

    @Override
    public void removeSurveyUnitsFromPartition(Long partitionId) {
        List<PartitionSurveyUnitLinkEntity> links =
                partitionSurveyUnitLinkRepository.findAllSurveyUnitsByPartitionId(partitionId);
        for (PartitionSurveyUnitLinkEntity link : links) {
            partitionSurveyUnitLinkRepository.delete(link);
        }
    }

}
