package connectingchips.samchips.mind.entity;

import connectingchips.samchips.global.audit.Auditable;
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


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long joinedMindId;

    @NotNull

    private Integer count = 0;

    @NotNull
    private Integer isJoining = 1;

    @NotNull
    private Boolean todayWrite = false;

    private Boolean keepJoin = false;

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

    public void updateIsJoining(Integer isJoining) {
        this.isJoining = isJoining;
    }

    public JoinedMind updateTodayWrite(Boolean todayWrite) {
        this.todayWrite = todayWrite;
        return this;
    }

    public void updateCount(Integer count) {
        this.count = count;
    }
    
    public void updateKeepJoin(boolean keepJoin) { this.keepJoin = keepJoin; }
}
