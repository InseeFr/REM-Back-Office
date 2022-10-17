package fr.insee.rem.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_EMPTY)
public class PhoneNumber implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -714140155012584835L;
    private Source source;
    private boolean favorite;
    private String numero;

}
