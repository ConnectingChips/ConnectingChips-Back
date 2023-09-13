package connectingchips.samchips.mindtype.entity;

import connectingchips.samchips.audit.Auditable;
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
@NoArgsConstructor
public class MindType extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mindTypeId;
    //parentId 확인후 수정 예정
    private String name;

    @OneToMany(mappedBy = "mindType", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Mind> minds = new ArrayList<>();

    @Builder
    public MindType(String name, List<Mind> minds) {
        this.name = name;
        this.minds = minds;
    }
}
