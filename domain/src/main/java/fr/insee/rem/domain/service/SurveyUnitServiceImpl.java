package fr.insee.rem.domain.service;

import java.util.List;
import java.util.Optional;

import fr.insee.rem.domain.dtos.SampleSurveyUnitDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.exception.SampleNotFoundException;
import fr.insee.rem.domain.exception.SettingsException;
import fr.insee.rem.domain.exception.SurveyUnitNotFoundException;
import fr.insee.rem.domain.exception.SurveyUnitsNotFoundException;
import fr.insee.rem.domain.ports.api.SurveyUnitServicePort;
import fr.insee.rem.domain.ports.spi.SamplePersistencePort;
import fr.insee.rem.domain.ports.spi.SampleSurveyUnitPersistencePort;
import fr.insee.rem.domain.ports.spi.SurveyUnitPersistencePort;
import fr.insee.rem.domain.records.SuIdMappingRecord;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SurveyUnitServiceImpl implements SurveyUnitServicePort {

    private SurveyUnitPersistencePort surveyUnitPersistencePort;

    private SampleSurveyUnitPersistencePort sampleSurveyUnitPersistencePort;

    private SamplePersistencePort samplePersistencePort;

    public SurveyUnitServiceImpl(
                                 SurveyUnitPersistencePort surveyUnitPersistencePort, SampleSurveyUnitPersistencePort sampleSurveyUnitPersistencePort, SamplePersistencePort samplePersistencePort) {
        this.surveyUnitPersistencePort = surveyUnitPersistencePort;
        this.sampleSurveyUnitPersistencePort = sampleSurveyUnitPersistencePort;
        this.samplePersistencePort = samplePersistencePort;
    }

    @Override
    public List<SampleSurveyUnitDto> importSurveyUnitsToSample(Long sampleId, List<SurveyUnitDto> suList) {
        if (suList == null || suList.isEmpty()) {
            log.error("domain: importSurveyUnitsToSample({}, no survey units)", sampleId);
            throw new SettingsException("No Survey units: empty list or null");
        }
        log.debug("domain: importSurveyUnitsToSample({}, {} survey units)", sampleId, suList.size());
        if (!samplePersistencePort.existsById(sampleId)) {
            throw new SampleNotFoundException(sampleId);
        }
        return sampleSurveyUnitPersistencePort.saveAll(sampleId, suList);
    }

    @Override
    public SampleSurveyUnitDto addSurveyUnitToSample(Long surveyUnitId, Long sampleId) {
        log.debug("domain: addSurveyUnitToSample({}, {})", surveyUnitId, sampleId);
        if (!samplePersistencePort.existsById(sampleId)) {
            throw new SampleNotFoundException(sampleId);
        }
        if (!surveyUnitPersistencePort.existsById(surveyUnitId)) {
            throw new SurveyUnitNotFoundException(surveyUnitId);
        }
        return sampleSurveyUnitPersistencePort.addSurveyUnitToSample(surveyUnitId, sampleId);
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
    public void removeSurveyUnitFromSample(Long surveyUnitId, Long sampleId) {
        log.debug("domain: removeSurveyUnitFromSample({},{})", surveyUnitId, sampleId);
        if (!samplePersistencePort.existsById(sampleId)) {
            throw new SampleNotFoundException(sampleId);
        }
        if (!surveyUnitPersistencePort.existsById(surveyUnitId)) {
            throw new SurveyUnitNotFoundException(surveyUnitId);
        }
        sampleSurveyUnitPersistencePort.removeSurveyUnitFromSample(surveyUnitId, sampleId);
    }

    @Override
    public SurveyUnitDto getSurveyUnitById(Long surveyUnitId) {
        log.debug("domain: getSurveyUnitById({})", surveyUnitId);
        Optional<SurveyUnitDto> findSurveyUnitDto = surveyUnitPersistencePort.findById(surveyUnitId);
        if (!findSurveyUnitDto.isPresent()) {
            throw new SurveyUnitNotFoundException(surveyUnitId);
        }
        return findSurveyUnitDto.get();
    }

    @Override
    public List<SampleSurveyUnitDto> getSurveyUnitsBySampleId(Long sampleId) {
        log.debug("domain: getSurveyUnitsBySampleId({})", sampleId);
        if (!samplePersistencePort.existsById(sampleId)) {
            throw new SampleNotFoundException(sampleId);
        }
        return sampleSurveyUnitPersistencePort.findSurveyUnitsBySampleId(sampleId);
    }

    @Override
    public List<Long> getSurveyUnitIdsBySampleId(Long sampleId) {
        log.debug("domain: getSurveyUnitIdsBySampleId({})", sampleId);
        if (!samplePersistencePort.existsById(sampleId)) {
            throw new SampleNotFoundException(sampleId);
        }
        return sampleSurveyUnitPersistencePort.findAllIdsBySampleId(sampleId);
    }

    @Override
    public List<SuIdMappingRecord> getIdMappingTableBySampleId(Long sampleId) {
        log.debug("domain: getIdMappingTableBySampleId({})", sampleId);
        if (!samplePersistencePort.existsById(sampleId)) {
            throw new SampleNotFoundException(sampleId);
        }
        return sampleSurveyUnitPersistencePort.findSuIdMappingBySampleId(sampleId);
    }

    @Override
    public int addSurveyUnitsToSample(List<Long> surveyUnitIds, Long sampleId) {
        if (surveyUnitIds == null || surveyUnitIds.isEmpty()) {
            log.error("domain: addSurveyUnitsToSample({}, no ids)", sampleId);
            throw new SettingsException("No ids: empty list or null");
        }
        log.debug("domain: addSurveyUnitsToSample({})", sampleId);
        if (!samplePersistencePort.existsById(sampleId)) {
            throw new SampleNotFoundException(sampleId);
        }
        List<Long> idsNotOk = surveyUnitIds.stream().filter(id -> !surveyUnitPersistencePort.existsById(id)).toList();
        if (!idsNotOk.isEmpty()) {
            throw new SurveyUnitsNotFoundException(idsNotOk);
        }
        surveyUnitIds.stream().forEach(id -> sampleSurveyUnitPersistencePort.addSurveyUnitToSample(id, sampleId));
        return surveyUnitIds.size();
    }

    @Override
    public boolean checkRepositoryId(List<SurveyUnitDto> surveyUnitDtos) {
        if (surveyUnitDtos == null) {
            return false;
        }
        return surveyUnitDtos.stream().anyMatch(su -> su.getRepositoryId() != null);
    }

    @Override
    public SurveyUnitDto updateSurveyUnit(SurveyUnitDto surveyUnitDto) {
        if (surveyUnitDto == null) {
            log.error("domain: updateSurveyUnit(no data error)");
            throw new SettingsException("SurveyUnit data empty");
        }
        if (surveyUnitDto.getRepositoryId() == null) {
            log.error("domain: updateSurveyUnit(no id error)");
            throw new SettingsException("Repository id empty");
        }
        Long repositoryId = surveyUnitDto.getRepositoryId();
        log.debug("domain: updateSurveyUnit {}", repositoryId);
        if (!surveyUnitPersistencePort.existsById(repositoryId)) {
            throw new SurveyUnitNotFoundException(repositoryId);
        }
        return surveyUnitPersistencePort.update(surveyUnitDto);
    }

}
