package connectingchips.samchips.joinedmind.entity;

import connectingchips.samchips.audit.Auditable;
import connectingchips.samchips.mind.entity.Mind;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
public class JoinedMind extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long joinedMindId;

    @NotNull
    private int count;

    @NotNull
    private int isJoining;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MIND_ID")
    private Mind mind;

    //TO-DO -> MEMBER ENTITY 구현 시 참여한 유저 PK 구현

}
