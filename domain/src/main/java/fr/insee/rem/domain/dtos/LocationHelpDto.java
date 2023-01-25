package fr.insee.rem.domain.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LocationHelpDto {

    private String cityCode;
    private String building;
    private String floor;
    private String staircase;
    private String door;
    private String iris;
    private String sector;
    private GPSLocation gpsCoordinates;
    private Boolean elevator;
    private Boolean cityPriorityDistrict;
}
