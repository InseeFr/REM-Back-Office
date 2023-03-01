package fr.insee.rem.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.rem.infrastructure.entity.Sample;

public interface SampleRepository extends JpaRepository<Sample, Long> {

    Optional<Sample> findByLabel(String label);

}
