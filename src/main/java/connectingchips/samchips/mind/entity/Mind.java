package connectingchips.samchips.mind.entity;

import connectingchips.samchips.joinedmind.entity.JoinedMind;
import connectingchips.samchips.mindtype.entity.MindType;
import connectingchips.samchips.user.domain.User;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Setter
@NoArgsConstructor
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
    private String introImage;
    @NotNull
    @Column(length = 255)
    private String pageImage;
    @NotNull
    @Column(length = 255)
    private String totalListImage;
    @NotNull
    @Column(length = 255)
    private String myListImage;


    @OneToMany(mappedBy = "mind", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<JoinedMind> joinedMinds = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "mind_type_id")
    private MindType mindType;


    @Builder
    public Mind(String name, String introduce, String writeFormat, String introImage, String pageImage, String totalListImage, String myListImage, MindType mindType) {
        this.name = name;
        this.introduce = introduce;
        this.writeFormat = writeFormat;
        this.introImage = introImage;
        this.pageImage = pageImage;
        this.totalListImage = totalListImage;
        this.myListImage = myListImage;
        this.mindType = mindType;
    }
}
