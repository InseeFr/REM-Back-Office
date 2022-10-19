package fr.insee.rem.dto;

import org.apache.commons.collections4.MultiValuedMap;

import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyUnitDto {

    @CsvBindByName(column = "annee_fideli")
    private String anneeFideli;
    @CsvBindByName(column = "ident_unite")
    private Long identUnite;
    @CsvBindByName(column = "type_unite")
    private String typeUnite;
    @CsvBindByName(column = "ident_up_fin")
    private String identUpFin;
    @CsvBindByName
    private Double alea;
    @CsvBindByName
    private String depcom;
    @CsvBindByName(column = "depcom_n")
    private String depcomN;
    @CsvBindByName(column = "imm_prioritaire")
    private String immPrioritaire;
    @CsvBindByName(column = "btq_prioritaire")
    private String btqPrioritaire;
    @CsvBindByName(column = "libvoie_prioritaire")
    private String libvoiePrioritaire;
    @CsvBindByName(column = "imm_supplementaire")
    private String immSupplementaire;
    @CsvBindByName(column = "btq_supplementaire")
    private String btqSupplementaire;
    @CsvBindByName(column = "libvoie_supplementaire")
    private String libvoieSupplementaire;
    @CsvBindByName(column = "compladr_aft")
    private String compladrAft;
    @CsvBindByName(column = "code_postal")
    private String codePostal;
    @CsvBindByName(column = "libelle_commune")
    private String libelleCommune;
    @CsvBindByName
    private String iris;
    @CsvBindByName
    private String qpv;
    @CsvBindByName(column = "prefixe_ref_cad")
    private String prefixeRefCad;
    @CsvBindByName(column = "ref_cad")
    private String refcad;
    @CsvBindByName
    private Double x;
    @CsvBindByName
    private Double y;
    @CsvBindByName
    private String bat;
    @CsvBindByName
    private String escalier;
    @CsvBindByName
    private String etage;
    @CsvBindByName
    private String porte;
    @CsvBindByName
    private Integer surface;
    @CsvBindByName
    private String ascenseur;
    @CsvBindByName(column = "annee_const")
    private Integer anneeConst;
    @CsvBindByName(column = "nature_log")
    private String natureLog;
    @CsvBindByName(column = "log_social")
    private Boolean logSocial;
    @CsvBindByName(column = "nb_piece_princ")
    private Integer nbPiecePrinc;
    @CsvBindByName(column = "statut_occupation")
    private String statutOccupation;
    @CsvBindByName(column = "date_entree_pers")
    private Integer dateEntreePers;
    @CsvBindByName(column = "nb_pers_log")
    private Integer nbPersLog;
    @CsvBindByName(column = "info_compl_lgt1")
    private String infoComplLgt1;
    @CsvBindByName(column = "info_compl_lgt2")
    private String infoComplLgt2;
    @CsvBindByName(column = "info_compl_lgt3")
    private String infoComplLgt3;
    @CsvBindByName(column = "info_compl_lgt4")
    private String infoComplLgt4;
    @CsvBindByName(column = "ident_ind_dec")
    private Long identIndDec;
    @CsvBindByName(column = "ident_ind_co")
    private Long identIndCo;
    @CsvBindByName
    private String rges;
    @CsvBindByName
    private String ssech;
    @CsvBindByName
    private String numfa;
    @CsvBindByName
    private String cle;
    @CsvBindByName
    private String le;
    @CsvBindByName
    private String bs;
    @CsvBindByName
    private String ec;
    @CsvBindByName(column = "pole_gestion_opale")
    private String poleGestionOpale;
    @CsvBindByName(column = "affectation_idep")
    private String affectationIdep;
    @CsvBindByName(column = "autres_zae")
    private String autresZae;
    @CsvBindByName
    private String noGrap;
    @CsvBindByName
    private String noLog;
    @CsvBindByName(column = "adr_rang")
    private String adrRang;
    @CsvBindByName(column = "logRang")
    private String logRang;
    @CsvBindByName
    private String posPM;
    @CsvBindByName
    private String achl;
    @CsvBindByName
    private String typL;
    @CsvBindByName(column = "surf_tranche")
    private String surfTranche;
    @CsvBindByName
    private String stocd;
    @CsvBindByName(column = "ann_emmenag")
    private String annEmmenag;
    @CsvBindByName(column = "image_bal")
    private String imageBal;
    @CsvBindByName(column = "sr_1")
    private String sr1;
    @CsvBindByName(column = "sr_2")
    private String sr2;
    @CsvBindByName(column = "sr_3")
    private String sr3;
    @CsvBindByName(column = "sr_4")
    private String sr4;
    @CsvBindByName(column = "sr_5")
    private String sr5;
    @CsvBindByName(column = "sr_6")
    private String sr6;
    @CsvBindAndJoinByName(column = ".+_\\d+", elementType = String.class)
    private MultiValuedMap<String, String> persons;

}