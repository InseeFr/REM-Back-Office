package fr.insee.rem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.insee.rem.entities.SurveyUnit;

@Repository
public interface SurveyUnitRepository extends JpaRepository<SurveyUnit, Long> {}
