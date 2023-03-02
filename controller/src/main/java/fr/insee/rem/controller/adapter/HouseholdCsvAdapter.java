package fr.insee.rem.controller.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import fr.insee.rem.controller.sources.HouseholdCsvSource;
import fr.insee.rem.controller.utils.CoordinateConversionUtils;
import fr.insee.rem.domain.dtos.AdditionalInformationDto;
import fr.insee.rem.domain.dtos.AddressDto;
import fr.insee.rem.domain.dtos.EmailDto;
import fr.insee.rem.domain.dtos.LocationHelpDto;
import fr.insee.rem.domain.dtos.OtherIdentifierDto;
import fr.insee.rem.domain.dtos.PersonDto;
import fr.insee.rem.domain.dtos.PhoneNumberDto;
import fr.insee.rem.domain.dtos.Source;
import fr.insee.rem.domain.dtos.SurveyUnitDto;
import fr.insee.rem.domain.dtos.TypeUnit;

@Service
public class HouseholdCsvAdapter {

    public SurveyUnitDto convert(HouseholdCsvSource h) {
        return SurveyUnitDto.builder().typeUnit(TypeUnit.HOUSEHOLD).externalId(h.getExternalId()).address(convertAddress(h)).persons(convertPersonList(h))
            .otherIdentifier(convertOtherIdentifier(h)).additionalInformations(convertAdditionalInformation(h)).build();
    }

    private List<AdditionalInformationDto> convertAdditionalInformation(HouseholdCsvSource h) {
        List<AdditionalInformationDto> addInfoList = new ArrayList<>();
        h.getAdditionalInfomations().asMap().forEach((k, v) -> {
            String value = v.iterator().next();
            if (StringUtils.isNotBlank(value)) {
                addInfoList.add(AdditionalInformationDto.builder().key(k).value(v.iterator().next()).build());
            }
        });
        return addInfoList;
    }

    private OtherIdentifierDto convertOtherIdentifier(HouseholdCsvSource h) {
        return OtherIdentifierDto.builder().rges(h.getRges()).ssech(h.getSsech()).numfa(h.getNumfa()).cle(h.getCle()).le(h.getLe()).ec(h.getEc()).bs(h.getBs())
            .nograp(h.getNoGrap()).nolog(h.getNoLog()).build();
    }

    private List<PersonDto> convertPersonList(HouseholdCsvSource h) {
        List<PersonDto> persons = new ArrayList<>();
        Map<String, String> personMap = new HashMap<>();
        h.getPersons().asMap().forEach((k, v) -> personMap.put(k, v.iterator().next()));
        Map<String, String> addInfoMap = new HashMap<>();
        h.getAdditionalInfomations().asMap().forEach((k, v) -> addInfoMap.put(k, v.iterator().next()));
        for (int i = 1; i <= 50; i ++ ) {
            PersonDto personDto =
                convertPerson(i, personMap, addInfoMap.get("type_unite"), addInfoMap.get("ident_ind_dec"), addInfoMap.get("ident_ind_co"), h.getExternalId());
            if (StringUtils.isNotBlank(personDto.getExternalId())) {
                persons.add(personDto);
            }
            else {
                break;
            }
        }

        return persons;
    }

    private PersonDto convertPerson(int index, Map<String, String> personMap, String typeUnite, String identDec, String identCo, String externalId) {
        String identInd = personMap.get("ident_ind_" + index);
        Boolean surveyed = false;
        Boolean main = false;
        if (identInd != null) {
            if ("INDIVIDU".equals(typeUnite) && identInd.equals(externalId)) {
                surveyed = true;
            }
            if (identInd.equals(identDec) || identInd.equals(identCo)) {
                main = true;
            }
        }
        List<EmailDto> emails = new ArrayList<>();
        if (StringUtils.isNotBlank(personMap.get("mel_" + index))) {
            EmailDto email = EmailDto.builder().favorite(false).source(Source.INITIAL).mailAddress(personMap.get("mel_" + index)).build();
            emails.add(email);
        }
        List<PhoneNumberDto> phones = new ArrayList<>();
        if (StringUtils.isNotBlank(personMap.get("tel_port_" + index))) {
            PhoneNumberDto number = PhoneNumberDto.builder().favorite(false).source(Source.INITIAL).number(personMap.get("tel_port_" + index)).build();
            phones.add(number);
        }
        if (StringUtils.isNotBlank(personMap.get("tel2_" + index))) {
            PhoneNumberDto number = PhoneNumberDto.builder().favorite(false).source(Source.INITIAL).number(personMap.get("tel2_" + index)).build();
            phones.add(number);
        }
        return PersonDto.builder().index(index).externalId(identInd).gender(personMap.get("sexe_" + index)).firstName(personMap.get("nom_nais_" + index))
            .lastName(personMap.get("nom_usage_" + index)).birthName(personMap.get("prenom_" + index))
            .dateOfBirth(buildDateOfBirth(personMap.get("anais_" + index), personMap.get("mnais_" + index), personMap.get("jnais_" + index))).surveyed(surveyed)
            .main(main).emails(emails.isEmpty() ? null : emails).phoneNumbers(phones.isEmpty() ? null : phones).build();
    }

    private AddressDto convertAddress(HouseholdCsvSource h) {
        return AddressDto.builder().streetNumber(h.getStreetNumber()).repetitionIndex(h.getRepetitionIndex()).streetName(h.getStreetName())
            .addressSupplement(h.getAddressSupplement()).cityName(h.getCityName()).zipCode(h.getZipCode()).locationHelp(convertLocationHelp(h)).build();
    }

    private LocationHelpDto convertLocationHelp(HouseholdCsvSource h) {
        return LocationHelpDto.builder().cityCode(h.getCityCode()).building(h.getBuilding()).floor(h.getFloor()).staircase(h.getStaircase()).door(h.getDoor())
            .iris(h.getIris()).sector(StringUtils.isNotBlank(h.getSectorUp()) ? h.getSectorUp() : h.getSectorZae())
            .gpsCoordinates(CoordinateConversionUtils.convertCoordinates(h.getX(), h.getY(), h.getCityCode())).elevator(buildBoolean(h.getElevator()))
            .cityPriorityDistrict(StringUtils.isNotBlank(h.getCityPriorityDistrict())).build();
    }

    private Boolean buildBoolean(String test) {
        Pattern pattern = Pattern.compile("1|O|oui|vrai|true|yes", Pattern.CASE_INSENSITIVE);
        if (StringUtils.isBlank(test)) {
            return null;// NOSONAR
        }
        else if (pattern.matcher(test).find()) {
            return true;
        }
        return false;
    }

    private String buildDateOfBirth(String year, String month, String day) {
        if (StringUtils.isBlank(year)) {
            return null;
        }
        else if (StringUtils.isBlank(month)) {
            return year;
        }
        else if (StringUtils.isBlank(day)) {
            month = month.length() == 1 ? "0" + month : month;
            return year + month;
        }
        else {
            month = month.length() == 1 ? "0" + month : month;
            day = day.length() == 1 ? "0" + day : day;
            return year + month + day;
        }
    }

}
