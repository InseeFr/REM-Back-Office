package fr.insee.rem.controller.adapter;

import fr.insee.rem.controller.sources.HouseholdCsvSource;
import fr.insee.rem.controller.utils.CoordinateConversionUtils;
import fr.insee.rem.domain.dtos.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HouseholdCsvAdapter {

    public SurveyUnitDto convert(HouseholdCsvSource h) {
        return SurveyUnitDto.builder().context(Context.HOUSEHOLD).externalId(h.getExternalId())
                .address(convertAddress(h)).persons(convertPersonList(h))
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
        return OtherIdentifierDto.builder().rges(h.getRges()).ssech(h.getSsech()).numfa(h.getNumfa()).cle(h.getCle())
                .le(h.getLe()).ec(h.getEc()).bs(h.getBs())
                .nograp(h.getNoGrap()).nolog(h.getNoLog()).noi(h.getNoi()).nole(h.getNole()).autre(h.getAutre()).build();
    }

    private List<PersonDto> convertPersonList(HouseholdCsvSource h) {
        List<PersonDto> persons = new ArrayList<>();
        Map<String, String> personMap = new HashMap<>();
        h.getPersons().asMap().forEach((k, v) -> personMap.put(k, v.iterator().next()));
        Map<String, String> addInfoMap = new HashMap<>();
        h.getAdditionalInfomations().asMap().forEach((k, v) -> addInfoMap.put(k, v.iterator().next()));
        for (int i = 1; i <= 50; i++) {
            String identInd = personMap.get("ident_ind_" + i);
            if (StringUtils.isNotBlank(identInd)) {
                PersonDto person = convertPerson(i, identInd, personMap);
                persons.add(person);
            }
        }

        return persons;
    }

    private PersonDto convertPerson(int index, String identInd, Map<String, String> personMap) {
        List<EmailDto> emails = new ArrayList<>();
        if (StringUtils.isNotBlank(personMap.get("mel_" + index))) {
            EmailDto email = EmailDto.builder().favorite(false).source(Source.INITIAL).mailAddress(personMap
                    .get("mel_" + index)).build();
            emails.add(email);
        }
        List<PhoneNumberDto> phones = new ArrayList<>();
        if (StringUtils.isNotBlank(personMap.get("tel_port_" + index))) {
            PhoneNumberDto number = PhoneNumberDto.builder().favorite(false).source(Source.INITIAL).number(personMap
                    .get("tel_port_" + index)).build();
            phones.add(number);
        }
        if (StringUtils.isNotBlank(personMap.get("tel2_" + index))) {
            PhoneNumberDto number = PhoneNumberDto.builder().favorite(false).source(Source.INITIAL).number(personMap
                    .get("tel2_" + index)).build();
            phones.add(number);
        }
        return PersonDto.builder().index(index).externalId(identInd).gender(personMap.get("sexe_" + index))
                .firstName(personMap.get("prenom_" + index)).lastName(personMap.get("nom_usage_" + index))
                .birthName(personMap.get("nom_nais_" + index)).dateOfBirth(buildDateOfBirth(personMap
                        .get("anais_" + index), personMap.get("mnais_" + index), personMap.get("jnais_" + index))).emails(emails
                        .isEmpty() ? null : emails)
                .phoneNumbers(phones.isEmpty() ? null : phones).build();
    }

    private AddressDto convertAddress(HouseholdCsvSource h) {
        return AddressDto.builder().streetNumber(h.getStreetNumber()).repetitionIndex(h.getRepetitionIndex())
                .streetName(h.getStreetName())
                .addressSupplement(h.getAddressSupplement()).cityName(h.getCityName()).zipCode(h.getZipCode())
                .locationHelp(convertLocationHelp(h)).build();
    }

    private LocationHelpDto convertLocationHelp(HouseholdCsvSource h) {
        return LocationHelpDto.builder().cityCode(h.getCityCode()).building(h.getBuilding()).floor(h.getFloor())
                .staircase(h.getStaircase()).door(h.getDoor())
                .iris(h.getIris()).sector(StringUtils.isNotBlank(h.getSectorUp()) ? h.getSectorUp() : h.getSectorZae())
                .gpsCoordinates(CoordinateConversionUtils.convertCoordinates(h.getX(), h.getY(), h.getCityCode()))
                .elevator(buildBoolean(h.getElevator()))
                .cityPriorityDistrict(buildBoolean(h.getCityPriorityDistrict())).build();
    }

    private Boolean buildBoolean(String test) {
        if (StringUtils.isBlank(test)) {
            return null;// NOSONAR
        }
        return "1".equals(test);
    }

    private String buildDateOfBirth(String year, String month, String day) {
        if (StringUtils.isBlank(year)) {
            return null;
        }
        if (StringUtils.isBlank(month)) {
            return year;
        }
        if (StringUtils.isBlank(day)) {
            month = StringUtils.leftPad(month, 2, '0');
            return year + month;
        }
        month = StringUtils.leftPad(month, 2, '0');
        day = StringUtils.leftPad(day, 2, '0');
        return year + month + day;
    }

}
