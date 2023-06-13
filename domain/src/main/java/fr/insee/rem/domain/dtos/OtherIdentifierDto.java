package fr.insee.rem.domain.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OtherIdentifierDto {

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
