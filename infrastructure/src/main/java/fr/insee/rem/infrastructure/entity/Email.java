package fr.insee.rem.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import fr.insee.rem.domain.dtos.Source;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(Include.NON_EMPTY)
public class Email implements Serializable {

    private static final long serialVersionUID = -7908018292639977756L;
    private Source source;
    private Boolean favorite;
    private String mailAddress;

}
