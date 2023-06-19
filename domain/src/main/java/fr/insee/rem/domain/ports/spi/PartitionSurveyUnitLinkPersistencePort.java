package fr.insee.rem.domain.ports.spi;

import fr.insee.rem.domain.dtos.PartitionSurveyUnitLinkDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.records.SuIdMappingRecord;

import java.util.List;

public interface PartitionSurveyUnitLinkPersistencePort {

    List<Long> findAllIdsByPartitionId(Long partitionId);

    List<PartitionSurveyUnitLinkDto> saveAll(Long partitionId, List<SurveyUnitDto> suList);

    PartitionSurveyUnitLinkDto addExistingSurveyUnitIntoPartition(Long surveyUnitId, Long partitionId);

    void removeSurveyUnitFromPartition(Long surveyUnitId, Long partitionId);

    List<PartitionSurveyUnitLinkDto> findSurveyUnitsByPartitionId(Long partitionId);

    List<SuIdMappingRecord> findSurveyUnitIdsMappingTableByPartitionId(Long partitionId);

}
