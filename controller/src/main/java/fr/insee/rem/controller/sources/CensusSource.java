package fr.insee.rem.controller.sources;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CensusSource {
    private String idinternaute;
    private String numvoiloc;
    private String bisterloc;
    private String typevoiloc;
    private String nomvoiloc;
    private String resloc;
    private String cloc;
    private String cpostloc;
    private String idenq;
    private String mail;
    private String identifiant;
    private JsonNode externals;
}
