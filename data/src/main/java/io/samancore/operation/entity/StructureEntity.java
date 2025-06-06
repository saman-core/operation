package io.samancore.operation.entity;

import io.samancore.operation.model.GeneralStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "structure")
public class StructureEntity  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "sequence-structure")
    @GenericGenerator(
        name = "sequence-structure",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "sq_structure"),
            @Parameter(name = "increment_size", value = "1")
        }
    )
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column( name= "name")
    String name;
    @Column( name= "entity_id")
    Long entityId;
    @Column( name= "parent_id")
    Long parentId;
    @Column( name= "relationship_id")
    Long relationshipId;
    @Column( name= "created_operation_id")
    Long createdOperationId;
    @Column( name= "deleted_operation_id")
    Long deletedOperationId;
    @Column( name= "status")
    GeneralStatus status;

    @OneToMany(mappedBy = "structure", cascade = CascadeType.PERSIST)
    private List<ReferenceEntity> references;
}
