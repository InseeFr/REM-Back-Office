package fr.insee.rem.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.io.Serializable;

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
