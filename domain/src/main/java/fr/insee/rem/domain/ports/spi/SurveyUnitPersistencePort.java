package fr.insee.rem.domain.ports.spi;

import java.util.Optional;

import fr.insee.rem.domain.dtos.SurveyUnitDto;

public interface SurveyUnitPersistencePort {

    void deleteById(Long surveyUnitId);

    Optional<SurveyUnitDto> findById(Long surveyUnitId);

    boolean existsById(Long surveyUnitId);

}
