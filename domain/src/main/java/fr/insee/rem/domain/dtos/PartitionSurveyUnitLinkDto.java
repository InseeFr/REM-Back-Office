package fr.insee.rem.domain.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class PartitionSurveyUnitLinkDto {

    private PartitionDto partition;

    private SurveyUnitDto surveyUnit;

    @Builder.Default
    private final Date registeredDate = new Date();

}
