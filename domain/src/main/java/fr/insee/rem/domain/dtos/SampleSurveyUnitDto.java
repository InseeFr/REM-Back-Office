package fr.insee.rem.domain.dtos;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SampleSurveyUnitDto {

    private SampleDto sample;

    private SurveyUnitDto surveyUnit;

    @Builder.Default
    private final Date registeredDate = new Date();

}
