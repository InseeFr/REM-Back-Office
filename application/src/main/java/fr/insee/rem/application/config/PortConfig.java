package fr.insee.rem.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.insee.rem.domain.ports.api.SampleServicePort;
import fr.insee.rem.domain.ports.api.SurveyUnitServicePort;
import fr.insee.rem.domain.ports.spi.SamplePersistencePort;
import fr.insee.rem.domain.ports.spi.SampleSurveyUnitPersistencePort;
import fr.insee.rem.domain.ports.spi.SurveyUnitPersistencePort;
import fr.insee.rem.domain.service.SampleServiceImpl;
import fr.insee.rem.domain.service.SurveyUnitServiceImpl;
import fr.insee.rem.infrastructure.adapter.SampleJpaAdapter;
import fr.insee.rem.infrastructure.adapter.SampleSurveyUnitJpaAdapter;
import fr.insee.rem.infrastructure.adapter.SurveyUnitJpaAdapter;

@Configuration
public class PortConfig {

    @Bean
    public SamplePersistencePort samplePersistance() {
        return new SampleJpaAdapter();
    }

    @Bean
    public SampleSurveyUnitPersistencePort sampleSurveyUnitPersistance() {
        return new SampleSurveyUnitJpaAdapter();
    }

    @Bean
    public SurveyUnitPersistencePort surveyUnitPersistance() {
        return new SurveyUnitJpaAdapter();
    }

    @Bean
    public SampleServicePort sampleService() {
        return new SampleServiceImpl(samplePersistance());
    }

    @Bean
    public SurveyUnitServicePort surveyUnitService() {
        return new SurveyUnitServiceImpl(surveyUnitPersistance(), sampleSurveyUnitPersistance(), samplePersistance());
    }

}
