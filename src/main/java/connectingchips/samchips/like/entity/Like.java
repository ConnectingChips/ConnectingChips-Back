package connectingchips.samchips.like.entity;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.user.domain.User;
import jakarta.persistence.*;

@Entity
@Table(name = "Likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name ="board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;
}
