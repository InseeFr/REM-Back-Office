package fr.insee.rem.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "partition")
@Data
@NoArgsConstructor
public class PartitionEntity {


    @Id
    @GeneratedValue(generator = "seq_partition", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_partition", allocationSize = 1)
    private Long partitionId;

    @Column(unique = true)
    private String label;

    @OneToMany(mappedBy = "partition", orphanRemoval = false, cascade = CascadeType.REMOVE)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<PartitionSurveyUnitLinkEntity> partitionSurveyUnitLinkEntities = new HashSet<>();

    public PartitionEntity(Long partitionId, String label) {
        this.partitionId = partitionId;
        this.label = label;
    }
}
