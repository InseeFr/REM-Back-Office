package fr.insee.rem.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.insee.rem.infrastructure.entity.Sample;
import fr.insee.rem.infrastructure.entity.SampleSurveyUnit;
import fr.insee.rem.infrastructure.entity.SampleSurveyUnitPK;
import fr.insee.rem.infrastructure.entity.SurveyUnit;

@Repository
public interface SampleSurveyUnitRepository extends JpaRepository<SampleSurveyUnit, SampleSurveyUnitPK> {

    @Query(value = "select ssu.surveyUnit.id from SampleSurveyUnit ssu where ssu.sample.id = ?1")
    List<Long> findAllIdsBySampleId(Long sampleId);

    @Query(value = "select ssu from SampleSurveyUnit ssu where ssu.sample.id = ?1")
    List<SampleSurveyUnit> findBySampleId(Long sampleId);

    List<SampleSurveyUnit> findBySurveyUnit(SurveyUnit surveyUnit);

    @Query(value = "select ssu from SampleSurveyUnit ssu left join fetch ssu.surveyUnit su where ssu.sample = ?1")
    List<SampleSurveyUnit> findBySampleWithSurveyUnit(Sample sample);

}
