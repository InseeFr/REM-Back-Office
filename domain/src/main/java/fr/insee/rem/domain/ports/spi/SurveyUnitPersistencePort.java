package fr.insee.rem.domain.ports.spi;

import fr.insee.rem.domain.dtos.SurveyUnitDto;

import java.util.Optional;

public interface SurveyUnitPersistencePort {

    void deleteById(Long surveyUnitId);

    Optional<SurveyUnitDto> findById(Long surveyUnitId);

    boolean existsById(Long surveyUnitId);

    SurveyUnitDto update(SurveyUnitDto surveyUnit);

}
