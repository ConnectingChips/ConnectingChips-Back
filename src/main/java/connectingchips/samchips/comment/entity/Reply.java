package connectingchips.samchips.comment.entity;

import connectingchips.samchips.audit.Auditable;
import connectingchips.samchips.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Reply extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content")
    private String content;

    @Builder
    public Reply(Comment comment, User user, String content) {
        this.comment = comment;
        this.user = user;
        this.content = content;
    }
}
