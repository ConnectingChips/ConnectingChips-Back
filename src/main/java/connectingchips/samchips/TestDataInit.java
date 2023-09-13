package connectingchips.samchips;

import connectingchips.samchips.joinedmind.entity.JoinedMind;
import connectingchips.samchips.joinedmind.repository.JoinedMindRepository;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.repository.MindRepository;
import connectingchips.samchips.mindtype.entity.MindType;
import connectingchips.samchips.mindtype.repository.MindTypeRepository;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final MindRepository mindRepository;
    private final UserRepository userRepository;
    private final JoinedMindRepository joinedMindRepository;
    private final MindTypeRepository mindTypeRepository;
    public static final String MINDNAME = "작심이름";
    public static final String INTRODUCE = "소개글";
    public static final String BACKGROUND_IMAGE = "백그라운드 이미지";
    public static final String WRITE_FORMAT = "작성방식";
    public static final String EXAMPLE_IMAGE = "예시 이미지";
    public static final String NAME = "운동";
    private final Integer NUMBER = 1;
    private final String EMAIL = "email@naver.com";
    private final String ID = "id";
    private final String NICKNAME = "닉네임";
    private final String PASSWORD = "비밀번호";

    @PostConstruct
    public void init(){
        User user = User.builder()
                .accountId(ID)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .build();
        Mind mind = Mind.builder()
                .name(MINDNAME)
                .introduce(INTRODUCE)
                .backgroundImage(BACKGROUND_IMAGE)
                .writeFormat(WRITE_FORMAT)
                .exampleImage(EXAMPLE_IMAGE)
                .build();
        JoinedMind joinedMind = JoinedMind.builder()
                .count(NUMBER)
                .isJoining(NUMBER)
                .user(user)
                .mind(mind)
                .build();

        List<Mind> minds = new ArrayList<>();
        minds.add(mind);
        MindType mindType = MindType.builder()
                .name(NAME)
                .minds(minds)
                .build();
        mind.setJoinedMind(joinedMind);
        mind.setMindType(mindType);
        mind.setUser(user);
        userRepository.save(user);
        mindTypeRepository.save(mindType);
        mindRepository.save(mind);
        joinedMindRepository.save(joinedMind);
    }

}
