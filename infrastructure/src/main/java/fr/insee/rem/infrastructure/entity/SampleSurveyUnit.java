package fr.insee.rem.infrastructure.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "sample_survey_unit")
@Data
public class SampleSurveyUnit implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7546818970816616680L;

    @EmbeddedId
    private SampleSurveyUnitPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sampleId")
    private Sample sample;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("surveyUnitId")
    private SurveyUnit surveyUnit;

    @Column(name = "registered_date")
    @Temporal(TemporalType.DATE)
    private Date registeredDate;

    @SuppressWarnings("unused")
    private SampleSurveyUnit() {}

    public SampleSurveyUnit(Sample sample, SurveyUnit surveyUnit) {
        this.sample = sample;
        this.surveyUnit = surveyUnit;
        this.registeredDate = new Date();
        this.id = new SampleSurveyUnitPK(sample.getId(), surveyUnit.getRepositoryId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SampleSurveyUnit that = (SampleSurveyUnit) o;
        return Objects.equals(sample, that.sample) && Objects.equals(surveyUnit, that.surveyUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sample, surveyUnit);
    }

    @Override
    public String toString() {
        return "SampleSurveyUnit(id=" + id + ", createdOn=" + registeredDate + ")";
    }

}
