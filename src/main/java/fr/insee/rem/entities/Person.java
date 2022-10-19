package fr.insee.rem.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_EMPTY)

public class Person implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7165854120895777787L;

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

    private List<PhoneNumber> phonesNumbers;

    public Person(int index, Map<String, String> mapPersons) {
        this.index = index;
        this.identInd = mapPersons.get("ident_ind_" + index);
        this.nomUsage = mapPersons.get("nom_usage_" + index);
        this.nomNais = mapPersons.get("nom_nais_" + index);
        this.prenom = mapPersons.get("prenom_" + index);
        this.anais = mapPersons.get("anais_" + index);
        this.mnais = mapPersons.get("mnais_" + index);
        this.jnais = mapPersons.get("jnais_" + index);
        this.sexe = mapPersons.get("sexe_" + index);
        this.statuMatri = mapPersons.get("statu_matri_" + index);
        this.mel = mapPersons.get("mel_" + index);
        this.infoComplInd1 = mapPersons.get("info_compl_ind1_" + index);
        this.infoComplInd2 = mapPersons.get("info_compl_ind2_" + index);
        phonesNumbers = new ArrayList<>();
        if (StringUtils.isNotBlank(mapPersons.get("tel_port_" + index))) {
            PhoneNumber phoneNumber = new PhoneNumber(Source.FISCAL, false, mapPersons.get("tel_port_" + index));
            phonesNumbers.add(phoneNumber);
        }
        if (StringUtils.isNotBlank(mapPersons.get("tel2_" + index))) {
            PhoneNumber phoneNumber = new PhoneNumber(Source.FISCAL, false, mapPersons.get("tel2_" + index));
            phonesNumbers.add(phoneNumber);
        }
    }

}
