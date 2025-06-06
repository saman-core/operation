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

@Entity
@Getter
@Setter
@Table(name = "reference")
public class ReferenceEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "sequence-reference")
    @GenericGenerator(
            name = "sequence-reference",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "sq_reference"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column( name= "structure_id", insertable = false, updatable = false)
    Long structureId;
    @Column( name= "operation_id")
    Long operationId;
    @Column( name= "cde_id")
    Long cdeId;
    @Column( name= "cde_template")
    String cdeTemplate;
    @Column( name= "outsource_id", updatable = false, insertable = false)
    Long outsourceId;
    @Column( name= "status")
    GeneralStatus status;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "structure_id", referencedColumnName = "id")
    private StructureEntity structure;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "outsource_id", referencedColumnName = "id")
    private OutsourceEntity outsource;
}
