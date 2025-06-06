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
@Table(name = "transition_applied")
public class TransitionAppliedEntity  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "sequence-transition_applied")
    @GenericGenerator(
        name = "sequence-transition_applied",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "sq_transition_applied"),
            @Parameter(name = "increment_size", value = "1")
        }
    )
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column( name= "transition_id")
    Long transitionId;
    @Column( name= "cde_id")
    Long cdeId;
    @Column( name= "cde_template")
    String cdeTemplate;
}
