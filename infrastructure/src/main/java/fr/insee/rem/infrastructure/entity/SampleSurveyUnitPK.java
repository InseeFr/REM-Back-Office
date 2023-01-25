package fr.insee.rem.infrastructure.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class SampleSurveyUnitPK implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1449181380370389057L;

    @Column(name = "sample_id")
    private Long sampleId;

    @Column(name = "survey_unit_id")
    private Long surveyUnitId;

    @SuppressWarnings("unused")
    private SampleSurveyUnitPK() {}

    public SampleSurveyUnitPK(Long sampleId, Long surveyUnitId) {
        this.sampleId = sampleId;
        this.surveyUnitId = surveyUnitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SampleSurveyUnitPK that = (SampleSurveyUnitPK) o;
        return Objects.equals(sampleId, that.sampleId) && Objects.equals(surveyUnitId, that.surveyUnitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sampleId, surveyUnitId);
    }
}
