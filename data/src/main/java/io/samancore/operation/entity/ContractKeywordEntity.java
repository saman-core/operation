package io.samancore.operation.entity;

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
@Table(name = "contract_keyword")
public class ContractKeywordEntity  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "sequence-contract_keyword")
    @GenericGenerator(
        name = "sequence-contract_keyword",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "sq_contract_keyword"),
            @Parameter(name = "increment_size", value = "1")
        }
    )
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column( name= "contract_id")
    Long contractId;
    @Column( name= "name")
    String name;
    @Column( name= "value")
    String value;
}
