package fr.insee.rem.domain.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import fr.insee.rem.domain.views.Views;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Builder
@Data
public class SurveyUnitDto {

    @JsonView(value = {Views.SurveyUnitWithId.class, Views.SurveyUnitWithIdAndExternals.class})
    private Long repositoryId;
    @JsonView(Views.SurveyUnitBase.class)
    private String externalId;
    @JsonView(Views.SurveyUnitBase.class)
    private String externalName;
    @JsonView(Views.SurveyUnitBase.class)
    private Context context;
    @JsonView(Views.SurveyUnitBase.class)
    private AddressDto address;
    @Getter(AccessLevel.NONE)
    private List<PersonDto> persons;
    @JsonView(Views.SurveyUnitBase.class)
    private OtherIdentifierDto otherIdentifier;
    @JsonView(Views.SurveyUnitBase.class)
    private List<AdditionalInformationDto> additionalInformations;
    @JsonView(value = {Views.SurveyUnitWithExternals.class, Views.SurveyUnitWithIdAndExternals.class})
    private JsonNode externals;

    /**
     * Defining everyone's role (surveyed, main, coDeclarant)
     * for HouseHold Unit
     *
     * @return the persons
     */
    @JsonView(Views.SurveyUnitBase.class)
    public List<PersonDto> getPersons() {
        if (persons == null) {
            return List.of();
        }
        if (context == Context.BUSINESS) {
            return persons;
        }
        if (additionalInformations == null || additionalInformations.isEmpty()) {
            return persons;
        }
        Optional<String> typeUnite = getValueByKey("type_unite");
        boolean isIndividualUnit = typeUnite.isPresent() && "INDIVIDU".equalsIgnoreCase(typeUnite.get());
        Optional<String> mainId = getValueByKey("ident_ind_dec");
        Optional<String> coDeclarantId = getValueByKey("ident_ind_co");

        List<PersonDto> definedPersons = new ArrayList<>();
        for (PersonDto person : persons) {
            PersonDto definedPerson = person;
            if (isIndividualUnit && definedPerson.getSurveyed() == null && person
                    .getExternalId() != null && person.getExternalId().equalsIgnoreCase(externalId)) {
                definedPerson.setSurveyed(true);
            }
            if (mainId.isPresent() && definedPerson.getMain() == null && person
                    .getExternalId() != null && person.getExternalId().equalsIgnoreCase(mainId.get())) {
                definedPerson.setMain(true);
            }
            if (coDeclarantId.isPresent() && definedPerson.getCoDeclarant() == null && person
                    .getExternalId() != null && person.getExternalId().equalsIgnoreCase(coDeclarantId.get())) {
                definedPerson.setCoDeclarant(true);
            }
            definedPersons.add(definedPerson);
        }

        return definedPersons;
    }

    private Optional<String> getValueByKey(String key) {
        if (key == null) {
            return Optional.empty();
        }
        for (AdditionalInformationDto addInfo : additionalInformations) {
            if (addInfo.getKey() != null && key.equalsIgnoreCase(addInfo.getKey())) {
                return Optional.of(addInfo.getValue());
            }
        }
        return Optional.empty();
    }


}
