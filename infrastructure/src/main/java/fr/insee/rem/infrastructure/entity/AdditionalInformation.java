package fr.insee.rem.infrastructure.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class AdditionalInformation implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1278478308717480850L;

    private String key;
    private String value;
}
