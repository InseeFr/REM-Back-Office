package fr.insee.rem.domain.service.mock;

import fr.insee.rem.domain.dtos.SampleDto;
import fr.insee.rem.domain.dtos.SampleSurveyUnitDto;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.ports.spi.SampleSurveyUnitPersistencePort;
import fr.insee.rem.domain.records.SuIdMappingRecord;

import java.util.ArrayList;
import java.util.List;

public class SampleSurveyUnitPersistenceInMemory implements SampleSurveyUnitPersistencePort {

    public SampleSurveyUnitPersistenceInMemory(SamplePersistenceInMemory samplePersistenceInMemory, SurveyUnitPersistenceInMemory surveyUnitPersistenceInMemory) {
        this.samplePersistenceInMemory = samplePersistenceInMemory;
        this.surveyUnitPersistenceInMemory = surveyUnitPersistenceInMemory;
    }

    SurveyUnitPersistenceInMemory surveyUnitPersistenceInMemory;
    SamplePersistenceInMemory samplePersistenceInMemory;

    List<SampleSurveyUnitDto> sampleSurveyUnitsInMemory = new ArrayList<>();

    @Override
    public List<Long> findAllIdsBySampleId(Long sampleId) {
        return sampleSurveyUnitsInMemory.stream()
                .filter(ssu -> ssu.getSample().getId().equals(sampleId))
                .map(ssu -> ssu.getSurveyUnit().getRepositoryId())
                .toList();
    }

    @Override
    public List<SampleSurveyUnitDto> saveAll(Long sampleId, List<SurveyUnitDto> suList) {
        SampleDto existingSample = samplePersistenceInMemory.findById(sampleId).orElseThrow();
        List<SampleSurveyUnitDto> savedSampleSurveyUnits = new ArrayList<>();
        for (SurveyUnitDto su : suList) {
            su = surveyUnitPersistenceInMemory.save(su);
            savedSampleSurveyUnits.add(SampleSurveyUnitDto.builder().sample(existingSample).surveyUnit(su).build());
        }
        sampleSurveyUnitsInMemory.addAll(savedSampleSurveyUnits);
        return savedSampleSurveyUnits;
    }

    @Override
    public SampleSurveyUnitDto addSurveyUnitToSample(Long surveyUnitId, Long sampleId) {
        SampleDto sample = samplePersistenceInMemory.findById(sampleId).orElseThrow();
        SurveyUnitDto surveyUnit = surveyUnitPersistenceInMemory.findById(surveyUnitId).orElseThrow();
        SampleSurveyUnitDto ssu = SampleSurveyUnitDto.builder().sample(sample).surveyUnit(surveyUnit).build();
        sampleSurveyUnitsInMemory.add(ssu);
        return ssu;
    }

    @Override
    public void removeSurveyUnitFromSample(Long surveyUnitId, Long sampleId) {
        sampleSurveyUnitsInMemory.removeIf(ssu -> ssu.getSample().getId().equals(sampleId) && ssu.getSurveyUnit().getRepositoryId().equals(surveyUnitId));
    }

    @Override
    public List<SampleSurveyUnitDto> findSurveyUnitsBySampleId(Long sampleId) {
        return sampleSurveyUnitsInMemory.stream()
                .filter(ssu -> ssu.getSample().getId().equals(sampleId))
                .toList();
    }

    @Override
    public List<SuIdMappingRecord> findSuIdMappingBySampleId(Long sampleId) {
        return sampleSurveyUnitsInMemory.stream()
                .filter(ssu -> ssu.getSample().getId().equals(sampleId))
                .map(ssu -> new SuIdMappingRecord(ssu.getSurveyUnit().getRepositoryId(), ssu.getSurveyUnit().getExternalId()))
                .toList();
    }
}
