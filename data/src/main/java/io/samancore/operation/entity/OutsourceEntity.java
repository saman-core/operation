package io.samancore.operation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "outsource")
public class OutsourceEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "sequence-outsource")
    @GenericGenerator(
        name = "sequence-outsource",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "sq_outsource"),
            @Parameter(name = "increment_size", value = "1")
        }
    )
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column( name= "contract_id")
    Long contractId;
    @Column( name= "structure_id")
    Long structureId;
    @Column( name= "operation_id")
    Long operationId;
    @Column( name= "module")
    Long module;
}
