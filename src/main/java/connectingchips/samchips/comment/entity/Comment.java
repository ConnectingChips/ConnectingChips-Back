package connectingchips.samchips.comment.entity;

import connectingchips.samchips.audit.Auditable;
import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @Size(max = 400)
    private String content;

    @OneToMany(mappedBy = "comment", orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    @Builder
    public Comment(Board board, User user, String content) {
        this.board = board;
        this.user = user;
        this.content = content;
    }
}
