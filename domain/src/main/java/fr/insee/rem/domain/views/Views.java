package fr.insee.rem.domain.views;

public class Views {

    public interface SurveyUnitBase {
    }

    public interface SurveyUnitWithId extends SurveyUnitBase {
    }

    public interface SurveyUnitWithExternals extends SurveyUnitBase {
    }

    public interface SurveyUnitWithIdAndExternals extends SurveyUnitWithId {
    }

}
