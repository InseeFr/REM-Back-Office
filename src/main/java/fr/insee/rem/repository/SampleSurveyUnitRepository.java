package fr.insee.rem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.insee.rem.entities.Sample;
import fr.insee.rem.entities.SampleSurveyUnit;

@Repository
public interface SampleSurveyUnitRepository extends JpaRepository<SampleSurveyUnit, Long> {

    List<SampleSurveyUnit> findBySample(Sample sample);

}
