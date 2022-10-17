package fr.insee.rem.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sample_survey_unit")
@Getter
@Setter
public class SampleSurveyUnit implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7546818970816616680L;

    @Id
    @GeneratedValue(generator = "seq_sample_survey_unit", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_sample_survey_unit")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sample_id")
    private Sample sample;

    @ManyToOne
    @JoinColumn(name = "survey_unit_id")
    private SurveyUnit surveyUnit;

    @Column(name = "registered_date")
    @Temporal(TemporalType.DATE)
    private Date registeredDate;
}
