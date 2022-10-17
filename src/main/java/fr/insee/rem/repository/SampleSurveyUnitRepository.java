package fr.insee.rem.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.insee.rem.entities.Sample;
import fr.insee.rem.entities.SampleSurveyUnit;

@Repository
public interface SampleSurveyUnitRepository extends CrudRepository<SampleSurveyUnit, Long> {

    List<SampleSurveyUnit> findBySample(Sample sample);

}
