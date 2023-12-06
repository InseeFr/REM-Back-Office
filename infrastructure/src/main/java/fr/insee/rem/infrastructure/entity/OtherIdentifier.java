package fr.insee.rem.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class OtherIdentifier {

    private String numfa;
    private String rges;
    private String ssech;
    private String cle;
    private String le;
    private String ec;
    private String bs;
    private String nograp;
    private String nolog;
    private String noi;
    private String nole;
    private String autre;
}
