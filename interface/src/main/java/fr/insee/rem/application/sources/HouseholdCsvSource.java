package fr.insee.rem.application.sources;

import org.apache.commons.collections4.MultiValuedMap;

import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseholdCsvSource {

    @CsvIgnore
    private static final String REGEX_PERSON_COLUMN =
        "ident_ind_\\d+|nom_usage_\\d+|nom_nais_\\d+|prenom_\\d+|(a|m|j)nais_\\d+|sexe_\\d+|mel_\\d+|tel(_port|2)_\\d+";

    @CsvBindByName(column = "ident_unite")
    private String externalId;
    @CsvBindByName(column = "ident_up_fin")
    private String sectorUp;
    @CsvBindByName(column = "autres_zae")
    private String sectorZae;
    @CsvBindByName(column = "depcom_n")
    private String cityCode;
    @CsvBindByName(column = "imm_prioritaire")
    private String streetNumber;
    @CsvBindByName(column = "btq_prioritaire")
    private String repetitionIndex;
    @CsvBindByName(column = "libvoie_prioritaire")
    private String streetName;
    @CsvBindByName(column = "compladr_aft")
    private String addressSupplement;
    @CsvBindByName(column = "code_postal")
    private String zipCode;
    @CsvBindByName(column = "libelle_commune")
    private String cityName;
    @CsvBindByName
    private String iris;
    @CsvBindByName(column = "qpv")
    private String cityPriorityDistrict;
    @CsvBindByName
    private Double x;
    @CsvBindByName
    private Double y;
    @CsvBindByName(column = "bat")
    private String building;
    @CsvBindByName(column = "escalier")
    private String staircase;
    @CsvBindByName(column = "etage")
    private String floor;
    @CsvBindByName(column = "porte")
    private String door;
    @CsvBindByName(column = "ascenseur")
    private String elevator;
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
    private String noGrap;
    @CsvBindByName
    private String noLog;
    @CsvBindAndJoinByName(column = REGEX_PERSON_COLUMN, elementType = String.class)
    private MultiValuedMap<String, String> persons;
    @CsvBindAndJoinByName(column = ".+", elementType = String.class)
    private MultiValuedMap<String, String> additionalInfomations;

}