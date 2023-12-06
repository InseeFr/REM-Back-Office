package fr.insee.rem.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class PartitionSurveyUnitLinkPK {


    @Column(name = "partition_id")
    private Long partitionId;

    @Column(name = "survey_unit_id")
    private Long surveyUnitId;

    @SuppressWarnings("unused")
    private PartitionSurveyUnitLinkPK() {
    }

    public PartitionSurveyUnitLinkPK(Long partitionId, Long surveyUnitId) {
        this.partitionId = partitionId;
        this.surveyUnitId = surveyUnitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PartitionSurveyUnitLinkPK that = (PartitionSurveyUnitLinkPK) o;
        return Objects.equals(partitionId, that.partitionId) && Objects.equals(surveyUnitId, that.surveyUnitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partitionId, surveyUnitId);
    }
}
