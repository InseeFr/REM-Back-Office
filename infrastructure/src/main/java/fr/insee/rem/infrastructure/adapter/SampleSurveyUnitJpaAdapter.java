package fr.insee.rem.infrastructure.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insee.rem.domain.dtos.SampleSurveyUnitDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.ports.spi.SampleSurveyUnitPersistencePort;
import fr.insee.rem.domain.records.SuIdMappingRecord;
import fr.insee.rem.infrastructure.entity.Sample;
import fr.insee.rem.infrastructure.entity.SampleSurveyUnit;
import fr.insee.rem.infrastructure.entity.SurveyUnit;
import fr.insee.rem.infrastructure.mappers.SampleSurveyUnitMapper;
import fr.insee.rem.infrastructure.mappers.SurveyUnitMapper;
import fr.insee.rem.infrastructure.repository.SampleRepository;
import fr.insee.rem.infrastructure.repository.SampleSurveyUnitRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class SampleSurveyUnitJpaAdapter implements SampleSurveyUnitPersistencePort {

    @Autowired
    private SampleSurveyUnitRepository sampleSurveyUnitRepository;

    @Autowired
    private SampleRepository sampleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 100;

    @Override
    public List<SampleSurveyUnitDto> saveAll(Long sampleId, List<SurveyUnitDto> suListDto) {
        Optional<Sample> optionalSample = sampleRepository.findById(sampleId);
        List<SurveyUnit> suList = SurveyUnitMapper.INSTANCE.listDtoToListEntity(suListDto);
        Sample sample = optionalSample.orElseThrow();
        List<SampleSurveyUnit> ssuList = new ArrayList<>();
        for (int i = 0; i < suList.size(); i++) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
                sample = entityManager.merge(sample); // re-attach entity to the persistence context
            }
            SurveyUnit su = suList.get(i);
            entityManager.persist(su);
            SampleSurveyUnit ssu = new SampleSurveyUnit(sample, su);
            entityManager.persist(ssu);
            ssuList.add(ssu);
        }
        return SampleSurveyUnitMapper.INSTANCE.listEntityToListDto(ssuList);
    }

    @Override
    public List<Long> findAllIdsBySampleId(Long sampleId) {
        return sampleSurveyUnitRepository.findAllIdsBySampleId(sampleId);
    }

    @Override
    public SampleSurveyUnitDto addSurveyUnitToSample(Long surveyUnitId, Long sampleId) {
        Sample sample = entityManager.find(Sample.class, sampleId);
        SurveyUnit surveyUnit = entityManager.find(SurveyUnit.class, surveyUnitId);
        SampleSurveyUnit ssu = new SampleSurveyUnit(sample, surveyUnit);
        ssu = sampleSurveyUnitRepository.save(ssu);
        return SampleSurveyUnitMapper.INSTANCE.entityToDto(ssu);
    }

    @Override
    public void removeSurveyUnitFromSample(Long surveyUnitId, Long sampleId) {
        Sample sample = entityManager.find(Sample.class, sampleId);
        SurveyUnit surveyUnit = entityManager.find(SurveyUnit.class, surveyUnitId);
        SampleSurveyUnit ssu = new SampleSurveyUnit(sample, surveyUnit);
        sampleSurveyUnitRepository.delete(ssu);
    }

    @Override
    public List<SampleSurveyUnitDto> findSurveyUnitsBySampleId(Long sampleId) {
        List<SampleSurveyUnit> ssuList = sampleSurveyUnitRepository.findAllSurveyUnitsBySampleId(sampleId);
        return SampleSurveyUnitMapper.INSTANCE.listEntityToListDto(ssuList);
    }

    @Override
    public List<SuIdMappingRecord> findSuIdMappingBySampleId(Long sampleId) {
        return sampleSurveyUnitRepository.findSuIdMappingBySampleId(sampleId);
    }

}
