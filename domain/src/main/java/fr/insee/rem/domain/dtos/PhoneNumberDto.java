package fr.insee.rem.domain.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PhoneNumberDto {

    private Source source;
    private Boolean favorite;
    private String number;
}
