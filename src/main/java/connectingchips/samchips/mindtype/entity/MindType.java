package connectingchips.samchips.mindtype.entity;

import connectingchips.samchips.audit.Auditable;
import connectingchips.samchips.mind.entity.Mind;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
public class MindType extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mindTypeId;
    //parentId 확인후 수정 예정
    @Column
    private String name;

    @OneToMany(mappedBy = "mindType", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Mind> minds = new ArrayList<>();


}
