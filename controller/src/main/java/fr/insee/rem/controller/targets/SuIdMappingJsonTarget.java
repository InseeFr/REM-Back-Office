package fr.insee.rem.controller.targets;

import fr.insee.rem.domain.records.SuIdMappingRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuIdMappingJsonTarget {
    private long partitionId;
    private List<SuIdMappingRecord> data;
    private int count;
}
