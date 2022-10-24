package fr.insee.rem.dto;

import org.springframework.beans.BeanUtils;

import fr.insee.rem.entities.PhoneNumber;
import fr.insee.rem.entities.Source;
import lombok.Data;

@Data
public class PhoneNumberDto {

    private Source source;
    private boolean favorite;
    private String numero;

    public PhoneNumberDto(PhoneNumber pn) {
        BeanUtils.copyProperties(pn, this);
    }

}
