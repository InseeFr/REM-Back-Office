package fr.insee.rem.domain.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SampleDto {

    private Long id;

    private String label;

}
