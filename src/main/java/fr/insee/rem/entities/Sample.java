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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @SequenceGenerator(name = "seq_sample", allocationSize = 1)
    private Long id;

    private String label;

    @OneToMany(mappedBy = "sample", orphanRemoval = true)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<SampleSurveyUnit> sampleSurveyUnit = new HashSet<>();
}
