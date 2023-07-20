package fr.insee.rem.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class LocationHelp {
    private String cityCode;
    private String building;
    private String floor;
    private String staircase;
    private String door;
    private String iris;
    private String sector;
    private Double latitude;
    private Double longitude;
    private Boolean elevator;
    private Boolean cityPriorityDistrict;
}
