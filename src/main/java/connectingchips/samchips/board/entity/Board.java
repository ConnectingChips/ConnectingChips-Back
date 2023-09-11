package connectingchips.samchips.board.entity;

import connectingchips.samchips.mind.entity.Mind;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;

    @NotNull
    private String content;

    @NotNull
    private String image;

    @ManyToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private User userId;

    @OneToOne(mappedBy = "mind", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Mind mindId;

    @CreatedDate
    @NotNull
    private LocalDateTime createDate;

    @LastModifiedDate
    @NotNull
    private LocalDateTime modifiedDate;
}
