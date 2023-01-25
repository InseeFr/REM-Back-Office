package fr.insee.rem.domain.ports.api;

import java.util.List;

import fr.insee.rem.domain.dtos.SampleDto;
import fr.insee.rem.domain.exception.SampleAlreadyExistsException;
import fr.insee.rem.domain.exception.SampleNotFoundException;

public interface SampleServicePort {

    void deleteSampleById(Long sampleId) throws SampleNotFoundException;

    SampleDto createSample(String label) throws SampleAlreadyExistsException;

    SampleDto getSampleById(Long sampleId) throws SampleNotFoundException;

    List<SampleDto> getAllSamples();

}
