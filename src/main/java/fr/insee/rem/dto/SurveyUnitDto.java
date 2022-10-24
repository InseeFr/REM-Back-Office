package fr.insee.rem.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import fr.insee.rem.entities.SurveyUnitData;
import lombok.Data;

@Data
public class SurveyUnitDto {

    private Long id;

    private List<PersonDto> persons;

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

    public SurveyUnitDto(Long id, SurveyUnitData su) {
        this.id = id;
        this.persons = su.getPersons().stream().map(p -> new PersonDto(p)).collect(Collectors.toList());
        BeanUtils.copyProperties(su, this);
    }
}
