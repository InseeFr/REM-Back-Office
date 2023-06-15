package fr.insee.rem.domain.dtos;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PersonDto {

    private Integer index;
    private String externalId;
    private String function;
    private String gender;
    private String firstName;
    private String lastName;
    private String birthName;
    private String dateOfBirth;
    private Boolean surveyed;
    private Boolean main;
    private Boolean coDeclarant;
    private List<PhoneNumberDto> phoneNumbers;
    private List<EmailDto> emails;
    private AddressDto address;

}
