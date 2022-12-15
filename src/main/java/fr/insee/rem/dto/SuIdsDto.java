package fr.insee.rem.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuIdsDto {

    private Long sampleId;
    private String sampleLabel;
    private List<Long> listOfIds;
}
