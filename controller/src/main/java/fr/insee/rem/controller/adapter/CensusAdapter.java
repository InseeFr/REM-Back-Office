package fr.insee.rem.controller.adapter;

import fr.insee.rem.controller.sources.CensusSource;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import org.springframework.stereotype.Service;

@Service
public class CensusAdapter {
    public SurveyUnitDto convert(CensusSource c) {

        return SurveyUnitDto.builder().build();
    }
}
