package fr.insee.rem.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.insee.rem.domain.records.SuIdMappingRecord;
import fr.insee.rem.infrastructure.entity.SampleSurveyUnit;
import fr.insee.rem.infrastructure.entity.SampleSurveyUnitPK;
import fr.insee.rem.infrastructure.entity.SurveyUnit;

public interface SampleSurveyUnitRepository extends JpaRepository<SampleSurveyUnit, SampleSurveyUnitPK> {

    @Query(value = "select ssu.surveyUnit.id from SampleSurveyUnit ssu where ssu.sample.id = ?1")
    List<Long> findAllIdsBySampleId(Long sampleId);

    @Query(value = "select ssu from SampleSurveyUnit ssu where ssu.sample.id = ?1")
    List<SampleSurveyUnit> findBySampleId(Long sampleId);

    List<SampleSurveyUnit> findBySurveyUnit(SurveyUnit surveyUnit);

    @Query(value = "select ssu from SampleSurveyUnit ssu left join fetch ssu.surveyUnit su where ssu.sample.id = ?1")
    List<SampleSurveyUnit> findAllSurveyUnitsBySampleId(Long sampleId);

    @Query(
            value = "select new fr.insee.rem.domain.records.SuIdMappingRecord(su.repositoryId, su.externalId) " + "from SampleSurveyUnit ssu, SurveyUnit su where ssu.surveyUnit.repositoryId = su.repositoryId and ssu.sample.id = ?1")
    List<SuIdMappingRecord> findSuIdMappingBySampleId(Long sampleId);

}
