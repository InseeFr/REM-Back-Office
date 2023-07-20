package fr.insee.rem.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "partition_survey_unit_link")
@Data
public class PartitionSurveyUnitLinkEntity {


    @EmbeddedId
    private PartitionSurveyUnitLinkPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("partitionId")
    @JoinColumn(name = "partition_id")
    private PartitionEntity partition;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("surveyUnitId")
    @JoinColumn(name = "survey_unit_id")
    private SurveyUnitEntity surveyUnit;

    @Column(name = "registered_date")
    @Temporal(TemporalType.DATE)
    private Date registeredDate;

    @SuppressWarnings("unused")
    private PartitionSurveyUnitLinkEntity() {
    }

    public PartitionSurveyUnitLinkEntity(PartitionEntity partition, SurveyUnitEntity surveyUnit) {
        this.partition = partition;
        this.surveyUnit = surveyUnit;
        this.registeredDate = new Date();
        this.id = new PartitionSurveyUnitLinkPK(partition.getPartitionId(), surveyUnit.getRepositoryId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PartitionSurveyUnitLinkEntity that = (PartitionSurveyUnitLinkEntity) o;
        return Objects.equals(partition, that.partition) && Objects.equals(surveyUnit,
                that.surveyUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partition, surveyUnit);
    }

    @Override
    public String toString() {
        return "PartitionSurveyUnitLinkEntity(id=" + id + ", createdOn=" + registeredDate + ")";
    }

}
