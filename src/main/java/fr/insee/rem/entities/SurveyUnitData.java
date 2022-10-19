package fr.insee.rem.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import fr.insee.rem.dto.SurveyUnitDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_EMPTY)
public class SurveyUnitData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 811623138921050269L;

    private List<Person> persons;

    private String anneeFideli;

    private Long identUnite;

    private String typeUnite;

    private String identUpFin;

    private Double alea;

    private String depcom;

    private String depcomN;

    private String immPrioritaire;

    private String btqPrioritaire;

    private String libvoiePrioritaire;

    private String immSupplementaire;

    private String btqSupplementaire;

    private String libvoieSupplementaire;

    private String compladrAft;

    private String codePostal;

    private String libelleCommune;

    private String iris;

    private String qpv;

    private String prefixeRefCad;

    private String refcad;

    private Double x;

    private Double y;

    private String bat;

    private String escalier;

    private String etage;

    private String porte;

    private Integer surface;

    private String ascenseur;

    private Integer anneeConst;

    private String natureLog;

    private Boolean logSocial;

    private Integer nbPiecePrinc;

    private String statutOccupation;

    private Integer dateEntreePers;

    private Integer nbPersLog;

    private String infoComplLgt1;

    private String infoComplLgt2;

    private String infoComplLgt3;

    private String infoComplLgt4;

    private Long identIndDec;

    private Long identIndCo;

    private String rges;

    private String ssech;

    private String numfa;

    private String cle;

    private String le;

    private String bs;

    private String ec;

    private String poleGestionOpale;

    private String affectationIdep;

    private String autresZae;

    private String noGrap;

    private String noLog;

    private String adrRang;

    private String logRang;

    private String posPM;

    private String achl;

    private String typL;

    private String surfTranche;

    private String stocd;

    private String annEmmenag;

    private String imageBal;

    private String sr1;

    private String sr2;

    private String sr3;

    private String sr4;

    private String sr5;

    private String sr6;

    public SurveyUnitData(SurveyUnitDto dto) {
        this.persons = new ArrayList<>();
        Map<String, String> mapPersons = new HashMap<>();
        dto.getPersons().asMap().forEach((key, value) -> mapPersons.put(key, value.iterator().next()));
        for (var index = 1; index < 50; index ++ ) {
            if (StringUtils.isNotBlank(mapPersons.get("ident_ind_" + index))) {
                Person person = new Person(index, mapPersons);
                persons.add(person);
            }
        }
        BeanUtils.copyProperties(dto, this, "persons");

    }

}
