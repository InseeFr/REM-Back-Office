package fr.insee.rem.exception;

public class SampleSurveyUnitNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 3783013039351734977L;

    public SampleSurveyUnitNotFoundException(Long sampleId, Long surveyUnitId) {
        super(String.format("The link between sample [%s] and survey unit [%s] doesn't exist", sampleId, surveyUnitId));
    }

}
