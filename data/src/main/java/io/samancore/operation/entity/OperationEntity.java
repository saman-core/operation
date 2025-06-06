package io.samancore.operation.entity;
import io.samancore.operation.model.OperationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "operation")
public class OperationEntity  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "sequence-operation")
    @GenericGenerator(
        name = "sequence-operation",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "sq_operation"),
            @Parameter(name = "increment_size", value = "1")
        }
    )
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column( name= "contract_id")
    Long contractId;
    @Column( name= "parent_id")
    Long parentId;
    @Column( name= "transition_applied_id", insertable = false, updatable = false)
    Long transitionAppliedId;
    @Column( name= "created_date")
    Date createdDate;
    @Column( name= "applied_date")
    Date appliedDate;
    @Column( name= "status")
    OperationStatus status;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "transition_applied_id", referencedColumnName = "id")
    private TransitionAppliedEntity transitionApplied;

    @ManyToOne
    @JoinColumn(name = "contract_id", insertable = false, updatable = false)
    private ContractEntity contract;

}
