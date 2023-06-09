package fr.insee.rem.domain.service.mock;

import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.ports.spi.SurveyUnitPersistencePort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SurveyUnitPersistenceInMemory implements SurveyUnitPersistencePort {

    List<SurveyUnitDto> surveyUnitListInMemory = new ArrayList<>();

    Long sequence = 0L;

    @Override
    public void deleteById(Long surveyUnitId) {
        surveyUnitListInMemory.removeIf(su -> su.getRepositoryId().equals(surveyUnitId));
    }

    @Override
    public Optional<SurveyUnitDto> findById(Long surveyUnitId) {
        return surveyUnitListInMemory.stream().filter(su -> su.getRepositoryId().equals(surveyUnitId)).findFirst();
    }

    @Override
    public boolean existsById(Long surveyUnitId) {
        return surveyUnitListInMemory.stream().anyMatch(su -> su.getRepositoryId().equals(surveyUnitId));
    }

    @Override
    public SurveyUnitDto update(SurveyUnitDto surveyUnitDto) {
        surveyUnitListInMemory.removeIf(su -> su.getRepositoryId().equals(surveyUnitDto.getRepositoryId()));
        surveyUnitListInMemory.add(surveyUnitDto);
        return surveyUnitDto;
    }

    public SurveyUnitDto save(SurveyUnitDto su) {
        su.setRepositoryId(++sequence);
        surveyUnitListInMemory.add(su);
        return su;
    }
}
