package fr.insee.rem.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sample")
@Data
public class Sample implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1680693250888586880L;

    @Id
    @GeneratedValue(generator = "seq_sample", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_sample")
    private Long id;

    private String label;

    @OneToMany(mappedBy = "sample")
    private Set<SampleSurveyUnit> sampleSurveyUnit = new HashSet<>();
}
