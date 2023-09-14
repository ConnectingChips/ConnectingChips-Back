package connectingchips.samchips.mind.service;

import connectingchips.samchips.joinedmind.repository.JoinedMindRepository;
import connectingchips.samchips.mind.dto.service.CheckAllMindOutput;
import connectingchips.samchips.mind.dto.service.FindMindOutput;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.repository.MindRepository;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MindService {

    private final MindRepository mindRepository;
    private final JoinedMindRepository joinedMindRepository;
    private final UserRepository userRepository;


    public FindMindOutput findMind(long mindId) {
        return FindMindOutput
                .of(findVerifiedMind(mindId), joinedMindRepository.countJoinedMindUser(mindId));
        //한번할때마다 너무 큰 연산이 필요한거 같음 Mind에 카운트 수를 두는게 나아보임
    }

    public List<FindMindOutput> findMinds() {
        return mindRepository.findAll()
                .stream()
                .map(a -> FindMindOutput.of(a, joinedMindRepository.countJoinedMindUser(a.getMindId())))
                .toList();
    }


    public Mind findVerifiedMind(Long mindId) {
        Optional<Mind> findMindById = mindRepository.findById(mindId);
        return findMindById.orElseThrow(() ->
                new RuntimeException("존재하지 않는 작심 번호입니다."));
    }

//    public List<CheckAllMindOutput> checkTodayAll(Long userId) {
//        User user = findVerifiedUser(userId);
//        user.getJoinedMinds().stream().map(a-> new CheckAllMindOutput(a.getJoinedMindId(),))
//        user -> .getboard-> board에서 데이터를 역순으로해서 순차적으로 조회하면서 오늘작성한 모든 board가 가지고 있는 mindId값으로
//         JoinedMindId를 찾아서 반환하는 값을 true로 반환
//    }


    private User findVerifiedUser(Long userId) {
        Optional<User> findUserById = userRepository.findById(userId);
        return findUserById.orElseThrow(() ->
                new RuntimeException("존재하지 않는 유저 번호입니다."));
    }
}
