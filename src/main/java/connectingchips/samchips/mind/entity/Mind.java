package connectingchips.samchips.mind.entity;

import connectingchips.samchips.joinedmind.entity.JoinedMind;
import connectingchips.samchips.mindtype.entity.MindType;
import connectingchips.samchips.user.domain.User;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
public class Mind {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mindId;

    @NotNull
    @Column(length = 20)
    private String name;

    @NotNull
    @Column(length = 100)
    private String introduce;

    @NotNull
    @Column(length = 100)
    private String writeFormat;

    @NotNull
    @Column(length = 255)
    private String backgroundImage;

    @NotNull
    @Column(length = 255)
    private String exampleImage;

    @OneToOne(mappedBy = "mind", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private JoinedMind joinedMind;

    @ManyToOne
    @JoinColumn(name = "mind_type_id")
    private MindType mindType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
