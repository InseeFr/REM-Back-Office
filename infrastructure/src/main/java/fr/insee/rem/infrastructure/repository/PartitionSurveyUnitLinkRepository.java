package fr.insee.rem.infrastructure.repository;

import fr.insee.rem.domain.records.SuIdMappingRecord;
import fr.insee.rem.infrastructure.entity.PartitionSurveyUnitLinkEntity;
import fr.insee.rem.infrastructure.entity.PartitionSurveyUnitLinkPK;
import fr.insee.rem.infrastructure.entity.SurveyUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartitionSurveyUnitLinkRepository extends JpaRepository<PartitionSurveyUnitLinkEntity,
        PartitionSurveyUnitLinkPK> {

    @Query(value = "select link.surveyUnit.repositoryId from PartitionSurveyUnitLinkEntity link where link.partition" +
            ".partitionId" +
            " = ?1")
    List<Long> findAllIdsByPartitionId(Long partitionId);

    @Query(value = "select link from PartitionSurveyUnitLinkEntity link where link.partition.id = ?1")
    List<PartitionSurveyUnitLinkEntity> findByPartitionId(Long partitionId);

    List<PartitionSurveyUnitLinkEntity> findBySurveyUnit(SurveyUnitEntity surveyUnitEntity);

    @Query(value = "select link from PartitionSurveyUnitLinkEntity link left join fetch link.surveyUnit su where link" +
            ".partition.partitionId = ?1")
    List<PartitionSurveyUnitLinkEntity> findAllSurveyUnitsByPartitionId(Long partitionId);

    @Query(
            value = "select new fr.insee.rem.domain.records.SuIdMappingRecord(su.repositoryId, su.externalId) " +
                    "from PartitionSurveyUnitLinkEntity link, SurveyUnitEntity su where link.surveyUnit.repositoryId " +
                    "= " +
                    "su.repositoryId and link.partition.partitionId = ?1")
    List<SuIdMappingRecord> findSuIdMappingByPartitionId(Long partitionId);

}
