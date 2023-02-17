package fr.insee.rem.infrastructure.adapter;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.ports.spi.SurveyUnitPersistencePort;
import fr.insee.rem.infrastructure.entity.SurveyUnit;
import fr.insee.rem.infrastructure.mappers.SurveyUnitMapper;
import fr.insee.rem.infrastructure.repository.SurveyUnitRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class SurveyUnitJpaAdapter implements SurveyUnitPersistencePort {

    @Autowired
    private SurveyUnitRepository surveyUnitRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void deleteById(Long surveyUnitId) {
        surveyUnitRepository.deleteById(surveyUnitId);
    }

    @Override
    public Optional<SurveyUnitDto> findById(Long surveyUnitId) {
        Optional<SurveyUnit> su = surveyUnitRepository.findById(surveyUnitId);
        if (su.isPresent()) {
            return Optional.of(SurveyUnitMapper.INSTANCE.entityToDto(su.get()));
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsById(Long surveyUnitId) {
        return surveyUnitRepository.existsById(surveyUnitId);
    }

}
