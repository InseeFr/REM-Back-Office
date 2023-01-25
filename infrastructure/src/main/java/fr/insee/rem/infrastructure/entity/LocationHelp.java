package fr.insee.rem.infrastructure.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class LocationHelp implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2055977266574207443L;
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
