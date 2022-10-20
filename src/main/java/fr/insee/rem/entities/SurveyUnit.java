package fr.insee.rem.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import fr.insee.rem.dto.SurveyUnitCsvDto;
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
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class SurveyUnit implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5458959377889736946L;

    @Id
    @GeneratedValue(generator = "seq_survey_unit", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_survey_unit", allocationSize = 100)
    private Long id;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "data")
    @Basic(fetch = FetchType.LAZY)
    private SurveyUnitData surveyUnitData;

    @OneToMany(mappedBy = "surveyUnit", orphanRemoval = true)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<SampleSurveyUnit> sampleSurveyUnit = new HashSet<>();

    public SurveyUnit(SurveyUnitCsvDto dto) {
        this.surveyUnitData = new SurveyUnitData(dto);
    }

}
