package connectingchips.samchips.comment.entity;

import connectingchips.samchips.audit.Auditable;
import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content")
    private String content;

    @Builder
    public Comment(Board board, User user, String content) {
        this.board = board;
        this.user = user;
        this.content = content;
    }
}
