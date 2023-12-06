package fr.insee.rem.controller.response;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuIdMappingCsv {

    @CsvBindByName(column = "ID_REM")
    private String idRem;
    @CsvBindByName(column = "ID_SOURCE")
    private String idSource;
}
