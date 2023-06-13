package fr.insee.rem.domain.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailDto {

    private Source source;
    private Boolean favorite;
    private String mailAddress;
}
