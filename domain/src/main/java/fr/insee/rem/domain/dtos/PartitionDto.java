package fr.insee.rem.domain.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PartitionDto {

    private Long partitionId;

    private String label;

}
