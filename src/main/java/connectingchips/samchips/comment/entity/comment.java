package connectingchips.samchips.comment.entity;

import connectingchips.samchips.audit.Auditable;
import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.user.domain.User;
import jakarta.persistence.*;

@Entity
public class comment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content")
    private String content;
}
