package connectingchips.samchips.board.entity;

import connectingchips.samchips.audit.Auditable;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
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
    private Mind mind;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Board(String content, String image, Mind mind, User user){
        this.content = content;
        this.image = image;
        this.mind = mind;
        this.user = user;
    }

}
