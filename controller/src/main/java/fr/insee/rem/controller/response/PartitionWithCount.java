package fr.insee.rem.controller.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import fr.insee.rem.domain.dtos.PartitionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartitionWithCount {
    
    @JsonUnwrapped
    private PartitionDto partition;
    private long count;

}
