package fr.insee.rem.domain.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AdditionalInformationDto {

    private String key;
    private String value;
}
