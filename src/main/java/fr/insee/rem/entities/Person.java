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
    private String identFoyerFisc;
    private String ordreFoyerFisc;
    private String presAnprec;
    private String statuMatri;
    private String typeMenage;
    private String statuFisc;
    private Boolean presIndemniteChom;
    private Integer niveauVie;
    private Integer typeRevenuPrinc;
    private String mel;
    private String adresseDac;
    private String biloc;
    private Double poidsFideli;
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
        this.identFoyerFisc = mapPersons.get("ident_foyfisc_" + index);
        this.ordreFoyerFisc = mapPersons.get("ordre_foyfisc_" + index);
        this.presAnprec = mapPersons.get("pres_anprec_" + index);
        this.statuMatri = mapPersons.get("statu_matri_" + index);
        this.typeMenage = mapPersons.get("type_menage_" + index);
        this.statuFisc = mapPersons.get("statu_fisc_" + index);
        this.presIndemniteChom =
            StringUtils.isNotBlank(mapPersons.get("pres_indemnite_chom_" + index))
                ? Boolean.parseBoolean(mapPersons.get("pres_indemnite_chom_" + index)) : null;
        this.niveauVie = StringUtils.isNotBlank(mapPersons.get("niveau_vie_" + index)) ? Integer.parseInt(mapPersons.get("niveau_vie_" + index)) : null;
        this.typeRevenuPrinc =
            StringUtils.isNotBlank(mapPersons.get("type_revenu_princ_" + index)) ? Integer.parseInt(mapPersons.get("type_revenu_princ_" + index)) : null;
        this.mel = mapPersons.get("mel_" + index);
        this.adresseDac = mapPersons.get("adresse_dac_" + index);
        this.biloc = mapPersons.get("biloc_" + index);
        this.poidsFideli = StringUtils.isNotBlank(mapPersons.get("poids_fideli_" + index)) ? Double.parseDouble(mapPersons.get("poids_fideli_" + index)) : null;
        this.infoComplInd1 = mapPersons.get("info_compl_ind1_" + index);
        this.infoComplInd2 = mapPersons.get("info_compl_ind2_" + index);
        phonesNumbers = new ArrayList<>();
        if (StringUtils.isNotBlank(mapPersons.get("tel_port_" + index))) {
            PhoneNumber phoneNumber = new PhoneNumber(Source.UNKNOWN, false, mapPersons.get("tel_port_" + index));
            phonesNumbers.add(phoneNumber);
        }
        if (StringUtils.isNotBlank(mapPersons.get("tel2_" + index))) {
            PhoneNumber phoneNumber = new PhoneNumber(Source.UNKNOWN, false, mapPersons.get("tel2_" + index));
            phonesNumbers.add(phoneNumber);
        }
    }

}
