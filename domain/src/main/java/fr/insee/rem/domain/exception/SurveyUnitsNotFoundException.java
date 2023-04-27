package fr.insee.rem.domain.exception;

import java.util.List;

public class SurveyUnitsNotFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 3596348709107118365L;

    public SurveyUnitsNotFoundException(List<Long> ids) {
        super(String.format("SurveyUnits [%s] doesn't exist", ids));
    }

}
