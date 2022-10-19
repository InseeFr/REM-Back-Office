package fr.insee.rem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.insee.rem.entities.Sample;
import fr.insee.rem.entities.SurveyUnit;

@Repository
public interface SurveyUnitRepository extends JpaRepository<SurveyUnit, Long> {

    @Query("SELECT su FROM SurveyUnit su LEFT JOIN SampleSurveyUnit ssu ON (ssu.surveyUnit = su) WHERE ssu.sample = ?1")
    List<SurveyUnit> findAllBySample(Sample sample);
}
