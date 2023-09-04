package fr.insee.rem.application.config;

import fr.insee.rem.domain.ports.api.PartitionServicePort;
import fr.insee.rem.domain.ports.api.SurveyUnitServicePort;
import fr.insee.rem.domain.ports.spi.PartitionPersistencePort;
import fr.insee.rem.domain.ports.spi.PartitionSurveyUnitLinkPersistencePort;
import fr.insee.rem.domain.ports.spi.SurveyUnitPersistencePort;
import fr.insee.rem.domain.service.PartitionServiceImpl;
import fr.insee.rem.domain.service.SurveyUnitServiceImpl;
import fr.insee.rem.infrastructure.adapter.PartitionJpaAdapter;
import fr.insee.rem.infrastructure.adapter.PartitionSurveyUnitLinkJpaAdapter;
import fr.insee.rem.infrastructure.adapter.SurveyUnitJpaAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PortConfig {

    @Bean
    PartitionPersistencePort partitionPersistance() {
        return new PartitionJpaAdapter();
    }

    @Bean
    PartitionSurveyUnitLinkPersistencePort partitionSurveyUnitLinkPersistance() {
        return new PartitionSurveyUnitLinkJpaAdapter();
    }

    @Bean
    SurveyUnitPersistencePort surveyUnitPersistance() {
        return new SurveyUnitJpaAdapter();
    }

    @Bean
    PartitionServicePort partitionService() {
        return new PartitionServiceImpl(partitionPersistance(), partitionSurveyUnitLinkPersistance());
    }

    @Bean
    SurveyUnitServicePort surveyUnitService() {
        return new SurveyUnitServiceImpl(surveyUnitPersistance(), partitionSurveyUnitLinkPersistance(),
                partitionPersistance());
    }

}
