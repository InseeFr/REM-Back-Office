package fr.insee.rem.domain.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GPSLocation {
    private Double latitude;
    private Double longitude;
}
