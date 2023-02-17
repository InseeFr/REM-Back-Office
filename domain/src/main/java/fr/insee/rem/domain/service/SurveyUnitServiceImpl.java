package fr.insee.rem.domain.service;

import java.util.List;
import java.util.Optional;

import fr.insee.rem.domain.dtos.SampleSurveyUnitDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.exception.SampleNotFoundException;
import fr.insee.rem.domain.exception.SurveyUnitNotFoundException;
import fr.insee.rem.domain.ports.api.SurveyUnitServicePort;
import fr.insee.rem.domain.ports.spi.SamplePersistencePort;
import fr.insee.rem.domain.ports.spi.SampleSurveyUnitPersistencePort;
import fr.insee.rem.domain.ports.spi.SurveyUnitPersistencePort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SurveyUnitServiceImpl implements SurveyUnitServicePort {

    private SurveyUnitPersistencePort surveyUnitPersistencePort;

    private SampleSurveyUnitPersistencePort sampleSurveyUnitPersistencePort;

    private SamplePersistencePort samplePersistencePort;

    public SurveyUnitServiceImpl(
        SurveyUnitPersistencePort surveyUnitPersistencePort,
        SampleSurveyUnitPersistencePort sampleSurveyUnitPersistencePort,
        SamplePersistencePort samplePersistencePort) {
        this.surveyUnitPersistencePort = surveyUnitPersistencePort;
        this.sampleSurveyUnitPersistencePort = sampleSurveyUnitPersistencePort;
        this.samplePersistencePort = samplePersistencePort;
    }

    @Override
    public List<SampleSurveyUnitDto> importSurveyUnitsToSample(Long sampleId, List<SurveyUnitDto> suList) throws SampleNotFoundException {
        log.debug("domain: importSurveyUnitsToSample({}, {} survey units)", sampleId, suList.size());
        if ( !samplePersistencePort.existsById(sampleId)) {
            throw new SampleNotFoundException(sampleId);
        }
        return sampleSurveyUnitPersistencePort.saveAll(sampleId, suList);
    }

    @Override
    public SampleSurveyUnitDto addSurveyUnitToSample(Long surveyUnitId, Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException {
        log.debug("domain: addSurveyUnitToSample({}, {})", surveyUnitId, sampleId);
        if ( !samplePersistencePort.existsById(sampleId)) {
            throw new SampleNotFoundException(sampleId);
        }
        if ( !surveyUnitPersistencePort.existsById(surveyUnitId)) {
            throw new SurveyUnitNotFoundException(surveyUnitId);
        }
        return sampleSurveyUnitPersistencePort.addSurveyUnitToSample(surveyUnitId, sampleId);
    }

    @Override
    public void deleteSurveyUnitById(Long surveyUnitId) throws SurveyUnitNotFoundException {
        log.debug("domain: deleteSurveyUnitById({})", surveyUnitId);
        if ( !surveyUnitPersistencePort.existsById(surveyUnitId)) {
            throw new SurveyUnitNotFoundException(surveyUnitId);
        }
        surveyUnitPersistencePort.deleteById(surveyUnitId);
    }

    @Override
    public void removeSurveyUnitFromSample(Long surveyUnitId, Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException {
        log.debug("domain: removeSurveyUnitFromSample({},{})", surveyUnitId, sampleId);
        if ( !samplePersistencePort.existsById(sampleId)) {
            throw new SampleNotFoundException(sampleId);
        }
        if ( !surveyUnitPersistencePort.existsById(surveyUnitId)) {
            throw new SurveyUnitNotFoundException(surveyUnitId);
        }
        sampleSurveyUnitPersistencePort.removeSurveyUnitFromSample(surveyUnitId, sampleId);
    }

    @Override
    public SurveyUnitDto getSurveyUnitById(Long surveyUnitId) throws SurveyUnitNotFoundException {
        log.debug("domain: getSurveyUnitById({})", surveyUnitId);
        Optional<SurveyUnitDto> findSurveyUnitDto = surveyUnitPersistencePort.findById(surveyUnitId);
        if ( !findSurveyUnitDto.isPresent()) {
            throw new SurveyUnitNotFoundException(surveyUnitId);
        }
        return findSurveyUnitDto.get();
    }

    @Override
    public List<SampleSurveyUnitDto> getSurveyUnitsBySampleId(Long sampleId) throws SampleNotFoundException {
        log.debug("domain: getSurveyUnitsBySampleId({})", sampleId);
        if ( !samplePersistencePort.existsById(sampleId)) {
            throw new SampleNotFoundException(sampleId);
        }
        return sampleSurveyUnitPersistencePort.findSurveyUnitsBySampleId(sampleId);
    }

    @Override
    public List<Long> getSurveyUnitIdsBySampleId(Long sampleId) throws SampleNotFoundException {
        log.debug("domain: getSurveyUnitIdsBySampleId({})", sampleId);
        if ( !samplePersistencePort.existsById(sampleId)) {
            throw new SampleNotFoundException(sampleId);
        }
        return sampleSurveyUnitPersistencePort.findAllIdsBySampleId(sampleId);
    }

}
