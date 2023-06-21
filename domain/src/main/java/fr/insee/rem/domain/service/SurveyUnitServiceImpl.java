package fr.insee.rem.domain.service;

import fr.insee.rem.domain.dtos.PartitionSurveyUnitLinkDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.exception.PartitionNotFoundException;
import fr.insee.rem.domain.exception.SettingsException;
import fr.insee.rem.domain.exception.SurveyUnitNotFoundException;
import fr.insee.rem.domain.exception.SurveyUnitsNotFoundException;
import fr.insee.rem.domain.ports.api.SurveyUnitServicePort;
import fr.insee.rem.domain.ports.spi.PartitionPersistencePort;
import fr.insee.rem.domain.ports.spi.PartitionSurveyUnitLinkPersistencePort;
import fr.insee.rem.domain.ports.spi.SurveyUnitPersistencePort;
import fr.insee.rem.domain.records.SuIdMappingRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class SurveyUnitServiceImpl implements SurveyUnitServicePort {

    private SurveyUnitPersistencePort surveyUnitPersistencePort;

    private PartitionSurveyUnitLinkPersistencePort partitionSurveyUnitLinkPersistencePort;

    private PartitionPersistencePort partitionPersistencePort;

    public SurveyUnitServiceImpl(
            SurveyUnitPersistencePort surveyUnitPersistencePort,
            PartitionSurveyUnitLinkPersistencePort partitionSurveyUnitLinkPersistencePort,
            PartitionPersistencePort partitionPersistencePort) {
        this.surveyUnitPersistencePort = surveyUnitPersistencePort;
        this.partitionSurveyUnitLinkPersistencePort = partitionSurveyUnitLinkPersistencePort;
        this.partitionPersistencePort = partitionPersistencePort;
    }

    @Override
    public List<PartitionSurveyUnitLinkDto> importSurveyUnitsIntoPartition(Long partitionId,
                                                                           List<SurveyUnitDto> suList) {
        if (suList == null || suList.isEmpty()) {
            log.error("domain: importSurveyUnitsIntoPartition({}, List of survey units to import is empty or null)",
                    partitionId);
            throw new SettingsException("List of survey units to import is empty or null");
        }
        log.debug("domain: importSurveyUnitsIntoPartition({}, {} survey units)", partitionId, suList.size());
        if (!partitionPersistencePort.existsById(partitionId)) {
            throw new PartitionNotFoundException(partitionId);
        }
        return partitionSurveyUnitLinkPersistencePort.saveAll(partitionId, suList);
    }

    @Override
    public PartitionSurveyUnitLinkDto addExistingSurveyUnitIntoPartition(Long surveyUnitId, Long partitionId) {
        log.debug("domain: addExistingSurveyUnitIntoPartition({}, {})", surveyUnitId, partitionId);
        if (!partitionPersistencePort.existsById(partitionId)) {
            throw new PartitionNotFoundException(partitionId);
        }
        if (!surveyUnitPersistencePort.existsById(surveyUnitId)) {
            throw new SurveyUnitNotFoundException(surveyUnitId);
        }
        return partitionSurveyUnitLinkPersistencePort.addExistingSurveyUnitIntoPartition(surveyUnitId, partitionId);
    }

    @Override
    public void deleteSurveyUnitById(Long surveyUnitId) {
        log.debug("domain: deleteSurveyUnitById({})", surveyUnitId);
        if (!surveyUnitPersistencePort.existsById(surveyUnitId)) {
            throw new SurveyUnitNotFoundException(surveyUnitId);
        }
        surveyUnitPersistencePort.deleteById(surveyUnitId);
    }

    @Override
    public void removeSurveyUnitFromPartition(Long surveyUnitId, Long partitionId) {
        log.debug("domain: removeSurveyUnitFromPartition({},{})", surveyUnitId, partitionId);
        if (!partitionPersistencePort.existsById(partitionId)) {
            throw new PartitionNotFoundException(partitionId);
        }
        if (!surveyUnitPersistencePort.existsById(surveyUnitId)) {
            throw new SurveyUnitNotFoundException(surveyUnitId);
        }
        partitionSurveyUnitLinkPersistencePort.removeSurveyUnitFromPartition(surveyUnitId, partitionId);
    }

    @Override
    public SurveyUnitDto getSurveyUnitById(Long surveyUnitId) {
        log.debug("domain: getSurveyUnitById({})", surveyUnitId);
        Optional<SurveyUnitDto> findSurveyUnit = surveyUnitPersistencePort.findById(surveyUnitId);
        if (!findSurveyUnit.isPresent()) {
            throw new SurveyUnitNotFoundException(surveyUnitId);
        }
        return findSurveyUnit.get();
    }

    @Override
    public List<PartitionSurveyUnitLinkDto> getSurveyUnitsByPartitionId(Long partitionId) {
        log.debug("domain: getSurveyUnitsByPartitionId({})", partitionId);
        if (!partitionPersistencePort.existsById(partitionId)) {
            throw new PartitionNotFoundException(partitionId);
        }
        return partitionSurveyUnitLinkPersistencePort.findSurveyUnitsByPartitionId(partitionId);
    }

    @Override
    public List<Long> getSurveyUnitIdsByPartitionId(Long partitionId) {
        log.debug("domain: getSurveyUnitIdsByPartitionId({})", partitionId);
        if (!partitionPersistencePort.existsById(partitionId)) {
            throw new PartitionNotFoundException(partitionId);
        }
        return partitionSurveyUnitLinkPersistencePort.findAllIdsByPartitionId(partitionId);
    }

    @Override
    public List<SuIdMappingRecord> getSurveyUnitIdsMappingTableByPartitionId(Long partitionId) {
        log.debug("domain: getSurveyUnitIdsMappingTableByPartitionId({})", partitionId);
        if (!partitionPersistencePort.existsById(partitionId)) {
            throw new PartitionNotFoundException(partitionId);
        }
        return partitionSurveyUnitLinkPersistencePort.findSurveyUnitIdsMappingTableByPartitionId(partitionId);
    }

    @Override
    public int addExistingSurveyUnitsToPartition(List<Long> surveyUnitIds, Long partitionId) {
        if (surveyUnitIds == null || surveyUnitIds.isEmpty()) {
            log.error("domain: addExistingSurveyUnitsToPartition({}, List of survey units to add is empty or null)",
                    partitionId);
            throw new SettingsException("List of survey units to add is empty or null");
        }
        log.debug("domain: addExistingSurveyUnitsToPartition({})", partitionId);
        if (!partitionPersistencePort.existsById(partitionId)) {
            throw new PartitionNotFoundException(partitionId);
        }
        List<Long> idsNotOk = surveyUnitIds.stream().filter(id -> !surveyUnitPersistencePort.existsById(id)).toList();
        if (!idsNotOk.isEmpty()) {
            throw new SurveyUnitsNotFoundException(idsNotOk);
        }
        surveyUnitIds.stream().forEach(id -> partitionSurveyUnitLinkPersistencePort.addExistingSurveyUnitIntoPartition(id,
                partitionId));
        return surveyUnitIds.size();
    }

    @Override
    public boolean checkRepositoryId(List<SurveyUnitDto> surveyUnits) {
        if (surveyUnits == null) {
            return false;
        }
        return surveyUnits.stream().anyMatch(su -> su.getRepositoryId() != null);
    }

    @Override
    public SurveyUnitDto updateSurveyUnit(SurveyUnitDto surveyUnit) {
        if (surveyUnit == null) {
            log.error("domain: updateSurveyUnit(no data error)");
            throw new SettingsException("SurveyUnit data empty");
        }
        if (surveyUnit.getRepositoryId() == null) {
            log.error("domain: updateSurveyUnit(no id error)");
            throw new SettingsException("Repository id empty");
        }
        Long repositoryId = surveyUnit.getRepositoryId();
        log.debug("domain: updateSurveyUnit {}", repositoryId);
        if (!surveyUnitPersistencePort.existsById(repositoryId)) {
            throw new SurveyUnitNotFoundException(repositoryId);
        }
        return surveyUnitPersistencePort.update(surveyUnit);
    }

}
