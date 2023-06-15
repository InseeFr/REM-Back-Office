package fr.insee.rem.domain.dtos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
public class SurveyUnitDto {

    private Long repositoryId;

    private String externalId;

    private String externalName;

    private TypeUnit typeUnit;

    private AddressDto address;

    @Getter(AccessLevel.NONE)
    private List<PersonDto> persons;

    private OtherIdentifierDto otherIdentifier;

    private List<AdditionalInformationDto> additionalInformations;

    /**
     * Defining everyone's role (surveyed, main, coDeclarant)
     * for HouseHold Unit
     * 
     * @return the persons
     */
    public List<PersonDto> getPersons() {
        if (persons == null || persons.isEmpty()) {
            return Collections.emptyList();
        }
        if (typeUnit == TypeUnit.ENTERPRISE) {
            return persons;
        }
        if (additionalInformations == null || additionalInformations.isEmpty()) {
            return persons;
        }
        boolean isIndividualUnit = "INDIVIDU".equalsIgnoreCase(getValueByKey("type_unite"));
        String mainId = getValueByKey("ident_ind_dec");
        String coDeclarantId = getValueByKey("ident_ind_co");

        List<PersonDto> definedPersons = new ArrayList<>();
        for (PersonDto person : persons) {
            PersonDto definedPerson = person;
            if (definedPerson.getSurveyed() == null && isIndividualUnit && person
                .getExternalId() != null && person.getExternalId().equalsIgnoreCase(externalId)) {
                definedPerson.setSurveyed(true);
            }
            if (definedPerson.getMain() == null && mainId != null && person
                .getExternalId() != null && person.getExternalId().equalsIgnoreCase(mainId)) {
                definedPerson.setMain(true);
            }
            if (definedPerson.getCoDeclarant() == null && coDeclarantId != null && person
                .getExternalId() != null && person.getExternalId().equalsIgnoreCase(coDeclarantId)) {
                definedPerson.setCoDeclarant(true);
            }
            definedPersons.add(definedPerson);
        }

        return definedPersons;
    }

    private String getValueByKey(String key) {
        if (key == null) {
            return null;
        }
        for (AdditionalInformationDto addInfo : additionalInformations) {
            if (addInfo.getKey() == null) {
                return null;
            }
            if (key.equalsIgnoreCase(addInfo.getKey())) {
                return addInfo.getValue();
            }
        }
        return null;
    }


}
