package fr.insee.rem.domain.ports.api;

import fr.insee.rem.domain.dtos.PartitionSurveyUnitLinkDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.records.SuIdMappingRecord;

import java.util.List;

public interface SurveyUnitServicePort {

    List<PartitionSurveyUnitLinkDto> importSurveyUnitsIntoPartition(Long partitionId, List<SurveyUnitDto> suList);

    PartitionSurveyUnitLinkDto addExistingSurveyUnitIntoPartition(Long surveyUnitId, Long partitionId);

    void deleteSurveyUnitById(Long surveyUnitId);

    void removeSurveyUnitFromPartition(Long surveyUnitId, Long partitionId);

    SurveyUnitDto getSurveyUnitById(Long surveyUnitId);

    List<PartitionSurveyUnitLinkDto> getSurveyUnitsByPartitionId(Long partitionId);

    List<Long> getSurveyUnitIdsByPartitionId(Long partitionId);

    List<SuIdMappingRecord> getSurveyUnitIdsMappingTableByPartitionId(Long partitionId);

    int addExistingSurveyUnitsToPartition(List<Long> surveyUnitIds, Long partitionId);

    boolean checkRepositoryId(List<SurveyUnitDto> surveyUnits);

    SurveyUnitDto updateSurveyUnit(SurveyUnitDto surveyUnit);

    long countSurveyUnitsByPartition(Long partitionId);
}
