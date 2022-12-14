package fr.insee.rem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.insee.rem.entities.Sample;
import fr.insee.rem.entities.SampleSurveyUnit;
import fr.insee.rem.entities.SampleSurveyUnitPK;
import fr.insee.rem.entities.SurveyUnit;

@Repository
public interface SampleSurveyUnitRepository extends JpaRepository<SampleSurveyUnit, SampleSurveyUnitPK> {

    @Query(value = "select ssu.surveyUnit.id from SampleSurveyUnit ssu where ssu.sample.id = ?1")
    List<Long> findAllIdsBySample(Long sampleId);

    List<SampleSurveyUnit> findBySample(Sample sample);

    List<SampleSurveyUnit> findBySurveyUnit(SurveyUnit surveyUnit);

}
