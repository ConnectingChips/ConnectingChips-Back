package connectingchips.samchips.user.domain;

import connectingchips.samchips.audit.Auditable;
import connectingchips.samchips.joinedmind.entity.JoinedMind;
import connectingchips.samchips.mind.entity.Mind;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private Long userId;

    @NotNull
    private String accountId;   // 로그인 아이디

    @NotNull
    private String password;    // 비밀번호

    @NotNull
    private String nickname;    // 닉네임

    @NotNull
    private String email;   //이메일

    @NotNull
    private String profileImage;

    @NotNull
    private String gender;  // 성별

    @NotNull
    private String ageRange;    // 연령대

    @Enumerated
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Role roles;     // 권한

    private String refreshToken;  //리프레쉬 토큰

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<JoinedMind> joinedMinds = new ArrayList<>();

    @Builder
    public User(String accountId, String password, String nickname, String email, SocialType socialType) {
        this.accountId = accountId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = "default";
        this.gender = "default";
        this.ageRange = "default";
        this.socialType = socialType;
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
