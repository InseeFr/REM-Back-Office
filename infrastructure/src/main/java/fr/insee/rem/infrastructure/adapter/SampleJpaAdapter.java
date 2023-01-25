package fr.insee.rem.infrastructure.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insee.rem.domain.dtos.SampleDto;
import fr.insee.rem.domain.ports.spi.SamplePersistencePort;
import fr.insee.rem.infrastructure.entity.Sample;
import fr.insee.rem.infrastructure.mappers.SampleMapper;
import fr.insee.rem.infrastructure.repository.SampleRepository;

@Service
@Transactional
public class SampleJpaAdapter implements SamplePersistencePort {

    @Autowired
    private SampleRepository sampleRepository;

    @Override
    public void deleteById(Long sampleId) {
        sampleRepository.deleteById(sampleId);
    }

    @Override
    public boolean existsById(Long sampleId) {
        return sampleRepository.existsById(sampleId);
    }

    @Override
    public Optional<SampleDto> findById(Long sampleId) {
        Optional<Sample> sample = sampleRepository.findById(sampleId);
        if (sample.isPresent()) {
            return Optional.of(SampleMapper.INSTANCE.entityToDto(sample.get()));
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public List<SampleDto> findAll() {
        List<Sample> samples = sampleRepository.findAll();
        return SampleMapper.INSTANCE.listEntityToListDto(samples);
    }

    @Override
    public Optional<SampleDto> findByLabel(String label) {
        Optional<Sample> sample = sampleRepository.findByLabel(label);
        if (sample.isPresent()) {
            return Optional.of(SampleMapper.INSTANCE.entityToDto(sample.get()));
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public SampleDto save(SampleDto sampleDto) {
        Sample sample = SampleMapper.INSTANCE.dtoToEntity(sampleDto);
        sample = sampleRepository.save(sample);
        return SampleMapper.INSTANCE.entityToDto(sample);
    }

}
