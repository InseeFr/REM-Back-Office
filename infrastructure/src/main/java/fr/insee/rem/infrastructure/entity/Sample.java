package fr.insee.rem.infrastructure.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "sample")
@Data
@NoArgsConstructor
public class Sample implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1680693250888586880L;

    @Id
    @GeneratedValue(generator = "seq_sample", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_sample", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private String label;

    @OneToMany(mappedBy = "sample", orphanRemoval = false, cascade = CascadeType.REMOVE)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<SampleSurveyUnit> sampleSurveyUnits = new HashSet<>();

    public Sample(Long id, String label) {
        this.id = id;
        this.label = label;
    }
}
