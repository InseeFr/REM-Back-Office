package fr.insee.rem.infrastructure.repository;

import fr.insee.rem.infrastructure.entity.PartitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartitionRepository extends JpaRepository<PartitionEntity, Long> {

    Optional<PartitionEntity> findByLabel(String label);

}
