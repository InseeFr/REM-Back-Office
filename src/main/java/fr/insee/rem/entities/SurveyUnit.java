package fr.insee.rem.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.insee.rem.dto.SurveyUnitCsvDto;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
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
    private Long id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "data")
    @Basic(fetch = FetchType.LAZY)
    private SurveyUnitData surveyUnitData;

    @OneToMany(mappedBy = "surveyUnit", orphanRemoval = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<SampleSurveyUnit> sampleSurveyUnits = new HashSet<>();

    public SurveyUnit(SurveyUnitCsvDto dto) {
        this.surveyUnitData = new SurveyUnitData(dto);
    }

}
