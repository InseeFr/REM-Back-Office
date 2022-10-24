package fr.insee.rem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.insee.rem.entities.Sample;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {

}
