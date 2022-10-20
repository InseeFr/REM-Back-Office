package fr.insee.rem.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import fr.insee.rem.entities.Person;
import lombok.Data;

@Data
public class PersonDto {

    private Integer index;
    private String identInd;
    private String nomUsage;
    private String nomNais;
    private String prenom;
    private String anais;
    private String mnais;
    private String jnais;
    private String sexe;
    private String statuMatri;
    private String mel;
    private String infoComplInd1;
    private String infoComplInd2;

    private List<PhoneNumberDto> phonesNumbers;

    public PersonDto(Person p) {
        this.phonesNumbers = p.getPhonesNumbers().stream().map(pn -> new PhoneNumberDto(pn)).collect(Collectors.toList());
        BeanUtils.copyProperties(p, this);
    }

}
