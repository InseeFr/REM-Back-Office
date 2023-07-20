package fr.insee.rem.controller.adapter;

import fr.insee.rem.controller.sources.CensusSource;
import fr.insee.rem.domain.dtos.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CensusAdapter {
    public SurveyUnitDto convert(CensusSource c) {

        return SurveyUnitDto.builder().externalId(c.getIdinternaute()).context(Context.HOUSEHOLD)
                .address(convertAddress(c)).persons(convertPersons(c))
                .additionalInformations(convertAdditionalInformation(c))
                .externals(c.getExternals()).build();
    }

    private List<AdditionalInformationDto> convertAdditionalInformation(CensusSource c) {
        AdditionalInformationDto addInfo = AdditionalInformationDto.builder()
                .key("identifiantCompte").value(c.getIdentifiant()).build();
        return List.of(addInfo);
    }

    private List<PersonDto> convertPersons(CensusSource c) {
        EmailDto mail = EmailDto.builder().favorite(false).source(Source.INITIAL).mailAddress(c.getMail()).build();
        PersonDto main = PersonDto.builder().index(1).main(true).externalId(c.getIdenq()).emails(List.of(mail)).build();
        return List.of(main);
    }

    private AddressDto convertAddress(CensusSource c) {
        return AddressDto.builder().streetNumber(c.getNumvoiloc()).repetitionIndex(c.getBisterloc())
                .streetType(c.getTypevoiloc()).streetName(c.getNomvoiloc()).addressSupplement(c.getResloc())
                .cityName(c.getCloc()).zipCode(c.getCpostloc()).build();
    }
}
