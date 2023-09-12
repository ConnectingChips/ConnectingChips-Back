package connectingchips.samchips.board.entity;

import connectingchips.samchips.audit.Auditable;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Board extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;

    @NotNull
    private String content;

    @NotNull
    private String image;

    @ManyToOne
    @JoinColumn(name ="mind_id")
    private Mind mindId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
