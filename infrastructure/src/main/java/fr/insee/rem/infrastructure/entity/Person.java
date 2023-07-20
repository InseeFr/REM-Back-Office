package fr.insee.rem.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(Include.NON_EMPTY)
public class Person {

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

    private List<PhoneNumber> phoneNumbers = new ArrayList<>();
    private List<Email> emails = new ArrayList<>();

    private Address address;

}
