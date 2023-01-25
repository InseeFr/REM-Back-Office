package fr.insee.rem.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.insee.rem.infrastructure.entity.SurveyUnit;

@Repository
public interface SurveyUnitRepository extends JpaRepository<SurveyUnit, Long> {}
