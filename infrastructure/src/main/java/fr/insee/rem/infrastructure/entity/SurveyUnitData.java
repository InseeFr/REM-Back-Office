package fr.insee.rem.infrastructure.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class SurveyUnitData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 811623138921050269L;

    private Address address;

    private List<Person> persons;

    private OtherIdentifier otherIdentifier;

    private List<AdditionalInformation> additionalInformations;

}
