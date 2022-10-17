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
    @CsvBindByName(column = "identifiant_strate")
    private String identifiantStrate;
    @CsvBindByName(column = "filtre_strate")
    private String filtreStrate;
    @CsvBindByName(column = "type_unite")
    private String typeUnite;
    @CsvBindByName(column = "ident_up_fin")
    private String identUpFin;
    @CsvBindByName
    private Double alea;
    @CsvBindByName(column = "id_log_associe")
    private String idLogAssocie;
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
    @CsvBindByName(column = "origine_adr_prioritaire")
    private String origineAdrPrioritaire;
    @CsvBindByName(column = "imm_supplementaire")
    private String immSupplementaire;
    @CsvBindByName(column = "btq_supplementaire")
    private String btqSupplementaire;
    @CsvBindByName(column = "libvoie_supplementaire")
    private String libvoieSupplementaire;
    @CsvBindByName(column = "origine_adr_supplementaire")
    private String origineAdrSupplementaire;
    @CsvBindByName(column = "compladr_aft")
    private String compladrAft;
    @CsvBindByName(column = "compladr_foncier")
    private String compladrFoncier;
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
    @CsvBindByName(column = "type_res")
    private String typeRes;
    @CsvBindByName
    private Integer surface;
    @CsvBindByName(column = "nb_niveau")
    private String nbNiveau;
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
    @CsvBindByName
    private String dependance;
    @CsvBindByName(column = "type_bati")
    private Integer typeBati;
    @CsvBindByName(column = "nb_pers_log")
    private Integer nbPersLog;
    @CsvBindByName(column = "logement_fictif")
    private Boolean logementFictif;
    @CsvBindByName(column = "nb_log_adr")
    private Integer nbLogAdr;
    @CsvBindByName(column = "pres_cmt")
    private Boolean presCmt;
    @CsvBindByName(column = "cat_cmt")
    private String catCmt;
    @CsvBindByName(column = "nom_cmt")
    private String nomCmt;
    @CsvBindByName(column = "pres_rh")
    private String presRh;
    @CsvBindByName(column = "cat_rh")
    private String catRh;
    @CsvBindByName(column = "nom_rh")
    private String nomRh;
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
    @CsvBindByName
    private String v;
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
    @CsvBindByName
    private String cil;
    @CsvBindByName
    private String fil;
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
    @CsvBindByName(column = "pc_1")
    private String pc1;
    @CsvBindByName(column = "pc_2")
    private String pc2;
    @CsvBindByName(column = "pc_3")
    private String pc3;
    @CsvBindByName(column = "pc_4")
    private String pc4;
    @CsvBindByName(column = "pc_5")
    private String pc5;
    @CsvBindByName(column = "pc_6")
    private String pc6;
    @CsvBindAndJoinByName(column = ".+_\\d+", elementType = String.class)
    private MultiValuedMap<String, String> persons;

}