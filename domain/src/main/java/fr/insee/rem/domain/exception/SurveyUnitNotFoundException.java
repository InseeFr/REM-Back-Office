package fr.insee.rem.domain.exception;

public class SurveyUnitNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -6392109505374844497L;

    public SurveyUnitNotFoundException(Long id) {
        super(String.format("SurveyUnit [%s] doesn't exist", id));
    }

}
