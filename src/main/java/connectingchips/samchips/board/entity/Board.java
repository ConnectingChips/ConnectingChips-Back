package connectingchips.samchips.board.entity;

import connectingchips.samchips.audit.Auditable;
import connectingchips.samchips.comment.entity.Comment;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Board extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;

    @NotNull
    @Size(max = 800)
    private String content;

    @NotNull
    private String image;

    @ManyToOne
    @JoinColumn(name ="mind_id")
    private Mind mind;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Board(String content, String image, Mind mind, User user){
        this.content = content;
        this.image = image;
        this.mind = mind;
        this.user = user;
    }

    public void editContent(String content) {
        this.content = content;
    }
}
