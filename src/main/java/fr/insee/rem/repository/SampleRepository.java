package fr.insee.rem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.insee.rem.entities.Sample;

@Repository
public interface SampleRepository extends CrudRepository<Sample, Long> {

}
