package fr.insee.rem.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.rem.infrastructure.entity.SurveyUnit;

public interface SurveyUnitRepository extends JpaRepository<SurveyUnit, Long> {}
