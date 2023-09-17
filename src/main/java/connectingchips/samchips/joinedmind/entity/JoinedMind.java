package connectingchips.samchips.joinedmind.entity;

import connectingchips.samchips.audit.Auditable;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class JoinedMind extends Auditable {
    private final Integer FIRST_COUNT = 0;
    private final Integer FIRST_JOINING = 1;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long joinedMindId;

    @NotNull

    private Integer count = FIRST_COUNT;

    @NotNull
    private Integer isJoining = FIRST_JOINING;

    @NotNull
    private Boolean todayWrite = false;

    @ManyToOne
    @JoinColumn(name = "mind_id")
    private Mind mind;

    //TO-DO -> MEMBER ENTITY 구현 시 참여한 유저 PK 구현
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



    @Builder
    public JoinedMind(Mind mind, User user) {
        this.mind = mind;
        if(!this.mind.getJoinedMinds().contains(this)){
            this.mind.getJoinedMinds().add(this);
        }
        this.user = user;
        if(!this.user.getJoinedMinds().contains(this)){
            this.user.getJoinedMinds().add(this);
        }
    }


    public JoinedMind setIsJoining(Integer isJoining) {
        this.isJoining = isJoining;
        return this;
    }
}
