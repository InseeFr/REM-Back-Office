package fr.insee.rem.infrastructure.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)

public class Person implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7165854120895777787L;

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

    private List<PhoneNumber> phoneNumbers = new ArrayList<>();
    private List<Email> emails = new ArrayList<>();

    private Address address;

}
