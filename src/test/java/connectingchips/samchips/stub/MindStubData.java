package connectingchips.samchips.stub;

import connectingchips.samchips.mind.entity.JoinedMind;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.entity.MindType;
import connectingchips.samchips.user.domain.User;

public class MindStubData {
    public JoinedMind createJoinedMind(User user, Mind mind) {
        return JoinedMind.builder()
                .user(user)
                .mind(mind)
                .build();
    }
    public MindType createMindType() {
        return MindType.builder()
                .name("작심타입테스트")
                .build();
    }

    public Mind createMind(){
        MindType mindtype = createMindType();
        Mind mind = Mind.builder()
                .name("작심테스트")
                .mindType(mindtype)
                .writeFormat("작성방식예제")
                .introduce("소개 예제")
                .myListImage("mylist.jpg")
                .pageImage("page.jpg")
                .totalListImage("totallist.jpg")
                .introImage("intro.jpg")
                .exampleImage("example.jpg")
                .build();
        return mind;
    }
}
