package fr.insee.rem.infrastructure.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.insee.rem.domain.dtos.TypeUnit;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@Table(name = "survey_unit")
public class SurveyUnit implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5458959377889736946L;

    @Id
    @GeneratedValue(generator = "seq_survey_unit", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_survey_unit", allocationSize = 100)
    private Long repositoryId;

    private String externalId;

    private String externalName;

    private TypeUnit typeUnit;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "data")
    @Basic(fetch = FetchType.EAGER)
    private SurveyUnitData surveyUnitData;

    @OneToMany(mappedBy = "surveyUnit", orphanRemoval = false, cascade = CascadeType.REMOVE)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<SampleSurveyUnit> sampleSurveyUnits = new HashSet<>();
}
