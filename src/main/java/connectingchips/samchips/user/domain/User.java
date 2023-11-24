package connectingchips.samchips.user.domain;

import connectingchips.samchips.global.audit.Auditable;
import connectingchips.samchips.mind.entity.JoinedMind;
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
    private Long id;

    @NotNull
    private String accountId;   // 로그인 아이디

    @NotNull
    private String password;    // 비밀번호

    @NotNull
    private String nickname;    // 닉네임

    @NotNull
    private String email;   //이메일

    private String profileImage;

    private String gender;  // 성별

    private String ageRange;    // 연령대

    @Enumerated
    private SocialType socialType;

    private String roles;     // 권한

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.LAZY)
    private List<JoinedMind> joinedMinds = new ArrayList<>();

    @Builder
    public User(String accountId, String password, String nickname, String email, String profileImage, String gender, String ageRange, SocialType socialType) {
        this.accountId = accountId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.gender = gender;
        this.ageRange = ageRange;
        this.socialType = socialType;
        this.roles = Role.ROLE_USER.getRole();
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void updateInfo(String nickname){
        this.nickname = nickname;
    }

    public void updateJoinedMinds(List<JoinedMind> joinedMinds) {
        this.joinedMinds = joinedMinds;
    }
}
