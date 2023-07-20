package fr.insee.rem.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import fr.insee.rem.domain.dtos.Context;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "survey_unit")
public class SurveyUnitEntity {

    @Id
    @GeneratedValue(generator = "seq_survey_unit", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_survey_unit", allocationSize = 100)
    private Long repositoryId;

    private String externalId;

    private String externalName;

    private Context context;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "data")
    @Basic(fetch = FetchType.EAGER)
    private SurveyUnitData surveyUnitData;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json", name = "externals")
    private JsonNode externals;

    @OneToMany(mappedBy = "surveyUnit", orphanRemoval = false, cascade = CascadeType.REMOVE)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<PartitionSurveyUnitLinkEntity> partitionSurveyUnitLinkEntities = new HashSet<>();
}
