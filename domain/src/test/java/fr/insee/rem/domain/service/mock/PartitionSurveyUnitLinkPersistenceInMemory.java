package fr.insee.rem.domain.service.mock;

import fr.insee.rem.domain.dtos.PartitionDto;
import fr.insee.rem.domain.dtos.PartitionSurveyUnitLinkDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.ports.spi.PartitionSurveyUnitLinkPersistencePort;
import fr.insee.rem.domain.records.SuIdMappingRecord;

import java.util.ArrayList;
import java.util.List;

public class PartitionSurveyUnitLinkPersistenceInMemory implements PartitionSurveyUnitLinkPersistencePort {

    public PartitionSurveyUnitLinkPersistenceInMemory(PartitionPersistenceInMemory partitionPersistenceInMemory,
                                                      SurveyUnitPersistenceInMemory surveyUnitPersistenceInMemory) {
        this.partitionPersistenceInMemory = partitionPersistenceInMemory;
        this.surveyUnitPersistenceInMemory = surveyUnitPersistenceInMemory;
    }

    SurveyUnitPersistenceInMemory surveyUnitPersistenceInMemory;
    PartitionPersistenceInMemory partitionPersistenceInMemory;

    List<PartitionSurveyUnitLinkDto> partitionSurveyUnitLinks = new ArrayList<>();

    @Override
    public List<Long> findAllIdsByPartitionId(Long partitionId) {
        return partitionSurveyUnitLinks.stream()
                .filter(link -> link.getPartition().getPartitionId().equals(partitionId))
                .map(link -> link.getSurveyUnit().getRepositoryId())
                .toList();
    }

    @Override
    public List<PartitionSurveyUnitLinkDto> saveAll(Long partitionId, List<SurveyUnitDto> suList) {
        PartitionDto partition = partitionPersistenceInMemory.findById(partitionId).orElseThrow();
        List<PartitionSurveyUnitLinkDto> newPartitionSurveyUnitLinks = new ArrayList<>();
        for (SurveyUnitDto su : suList) {
            su = surveyUnitPersistenceInMemory.save(su);
            newPartitionSurveyUnitLinks.add(PartitionSurveyUnitLinkDto.builder().partition(partition).surveyUnit(su).build());
        }
        partitionSurveyUnitLinks.addAll(newPartitionSurveyUnitLinks);
        return newPartitionSurveyUnitLinks;
    }

    @Override
    public PartitionSurveyUnitLinkDto addExistingSurveyUnitIntoPartition(Long surveyUnitId, Long partitionId) {
        PartitionDto partition = partitionPersistenceInMemory.findById(partitionId).orElseThrow();
        SurveyUnitDto surveyUnit = surveyUnitPersistenceInMemory.findById(surveyUnitId).orElseThrow();
        PartitionSurveyUnitLinkDto link =
                PartitionSurveyUnitLinkDto.builder().partition(partition).surveyUnit(surveyUnit).build();
        partitionSurveyUnitLinks.add(link);
        return link;
    }

    @Override
    public void removeSurveyUnitFromPartition(Long surveyUnitId, Long partitionId) {
        partitionSurveyUnitLinks.removeIf(link -> link.getPartition().getPartitionId().equals(partitionId)
                && link.getSurveyUnit().getRepositoryId().equals(surveyUnitId));
    }

    @Override
    public List<PartitionSurveyUnitLinkDto> findSurveyUnitsByPartitionId(Long partitionId) {
        return partitionSurveyUnitLinks.stream()
                .filter(link -> link.getPartition().getPartitionId().equals(partitionId))
                .toList();
    }

    @Override
    public List<SuIdMappingRecord> findSurveyUnitIdsMappingTableByPartitionId(Long partitionId) {
        return partitionSurveyUnitLinks.stream()
                .filter(link -> link.getPartition().getPartitionId().equals(partitionId))
                .map(link -> new SuIdMappingRecord(link.getSurveyUnit().getRepositoryId(),
                        link.getSurveyUnit().getExternalId()))
                .toList();
    }

    @Override
    public long countByPartitionId(Long partitionId) {
        return partitionSurveyUnitLinks.stream()
                .filter(link -> link.getPartition().getPartitionId().equals(partitionId))
                .count();
    }

    @Override
    public void removeSurveyUnitsFromPartition(Long partitionId) {
        partitionSurveyUnitLinks.removeIf(link -> link.getPartition().getPartitionId().equals(partitionId));
    }
}
