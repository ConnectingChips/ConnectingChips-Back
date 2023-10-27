package connectingchips.samchips.mind.mindtype.entity;

import connectingchips.samchips.global.audit.Auditable;
import connectingchips.samchips.mind.entity.Mind;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class MindType extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mindTypeId;

    private String name;

    @OneToMany(mappedBy = "mindType", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<Mind> minds = new ArrayList<>();

    @Builder
    public MindType(String name, List<Mind> minds) {
        this.name = name;
        this.minds = minds;
    }

}
