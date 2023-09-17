package connectingchips.samchips.user.domain;

import connectingchips.samchips.audit.Auditable;
import connectingchips.samchips.joinedmind.entity.JoinedMind;
import connectingchips.samchips.mind.entity.Mind;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "\"user\"")
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "password")
    private String password;  // 비밀번호

    @Column(name = "nickname")
    private String nickname;  // 닉네임

    @Column(name = "email")
    private String email;  //이메일

    @Column(name = "profile_image")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role roles;

    @Column(name = "refresh_token")
    private String refreshToken;  //리프레쉬 토큰

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<JoinedMind> joinedMinds = new ArrayList<>();


    @Builder
    public User(String accountId, String password, String nickname, String email) {
        this.accountId = accountId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = "default";
        this.roles = Role.ROLE_USER;
    }

    public void editInfo(String nickname){
        this.nickname = nickname;
    }

    public void editRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void editJoinedMinds(List<JoinedMind> joinedMinds) {
        this.joinedMinds = joinedMinds;
    }
}
