package fr.insee.rem.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import fr.insee.rem.domain.dtos.Source;
import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class Email {
    private Source source;
    private Boolean favorite;
    private String mailAddress;

}
