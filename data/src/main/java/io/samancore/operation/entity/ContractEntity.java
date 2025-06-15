package io.samancore.operation.entity;

import io.samancore.operation.model.GeneralStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "contract")
public class ContractEntity  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "sequence-contract")
    @GenericGenerator(
        name = "sequence-contract",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "sq_contract"),
            @Parameter(name = "increment_size", value = "1")
        }
    )
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column( name= "product_id")
    Long productId;
    @Column( name= "last_operation_id")
    Long lastOperationId;
    @Column( name= "state_id")
    Long stateId;
    @Column( name= "created_date")
    Date createdDate;
    @Column( name= "last_updated_date")
    Date lastUpdatedDate;
    @Column( name= "status")
    GeneralStatus status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "contract_id")
    private List<ContractKeywordEntity> contractKeywords;
}
