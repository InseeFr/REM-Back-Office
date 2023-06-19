package fr.insee.rem.infrastructure.repository;

import fr.insee.rem.infrastructure.entity.SurveyUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyUnitRepository extends JpaRepository<SurveyUnitEntity, Long> {
}
