package fr.insee.rem.domain.service;

import java.util.List;
import java.util.Optional;

import fr.insee.rem.domain.dtos.SampleDto;
import fr.insee.rem.domain.exception.SampleAlreadyExistsException;
import fr.insee.rem.domain.exception.SampleNotFoundException;
import fr.insee.rem.domain.ports.api.SampleServicePort;
import fr.insee.rem.domain.ports.spi.SamplePersistencePort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SampleServiceImpl implements SampleServicePort {

    private SamplePersistencePort samplePersistencePort;

    public SampleServiceImpl(SamplePersistencePort samplePersistencePort) {
        this.samplePersistencePort = samplePersistencePort;
    }

    @Override
    public void deleteSampleById(Long sampleId) throws SampleNotFoundException {
        log.debug("domain: deleteSampleById({})", sampleId);
        if ( !samplePersistencePort.existsById(sampleId)) {
            throw new SampleNotFoundException(sampleId);
        }
        samplePersistencePort.deleteById(sampleId);
    }

    @Override
    public SampleDto createSample(String label) throws SampleAlreadyExistsException {
        log.debug("domain: createSample({})", label);
        Optional<SampleDto> findSampleDto = samplePersistencePort.findByLabel(label);
        if (findSampleDto.isPresent()) {
            throw new SampleAlreadyExistsException(label);
        }
        return samplePersistencePort.save(SampleDto.builder().label(label).build());
    }

    @Override
    public SampleDto getSampleById(Long sampleId) throws SampleNotFoundException {
        log.debug("domain: getSampleById({})", sampleId);
        Optional<SampleDto> findSampleDto = samplePersistencePort.findById(sampleId);
        if ( !findSampleDto.isPresent()) {
            throw new SampleNotFoundException(sampleId);
        }
        return findSampleDto.get();
    }

    @Override
    public List<SampleDto> getAllSamples() {
        log.debug("domain: getAllSamples()");
        return samplePersistencePort.findAll();
    }

}
