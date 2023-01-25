package fr.insee.rem.domain.dtos;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SurveyUnitDto {

    private Long repositoryId;

    private String externalId;

    private String externalName;

    private TypeUnit typeUnit;

    private AddressDto address;

    private List<PersonDto> persons;

    private OtherIdentifierDto otherIdentifier;

    private List<AdditionalInformationDto> additionalInformations;

}
