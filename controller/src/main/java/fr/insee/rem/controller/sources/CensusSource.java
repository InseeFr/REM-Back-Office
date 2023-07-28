package fr.insee.rem.controller.sources;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CensusSource {
    private Long id;
    private String numvoiloc;
    private String bisterloc;
    private String typevoiloc;
    private String nomvoiloc;
    private String resloc;
    private String car;
    private String cpostloc;
    private Long idInternaute;
    private String mail;
    private String identifiantCompte;
    private JsonNode externals;
}
