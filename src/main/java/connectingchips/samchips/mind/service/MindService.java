package connectingchips.samchips.mind.service;

import connectingchips.samchips.joinedmind.service.JoinedMindService;
import connectingchips.samchips.mind.dto.service.FindMindOutput;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.repository.MindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MindService {

    private final MindRepository mindRepository;
    private final JoinedMindService joinedMindService;


    public FindMindOutput findMind(long mindId) {
        return FindMindOutput
                .of(findVerifiedMind(mindId), joinedMindService.countJoinedMindUser(mindId));
        //한번할때마다 너무 큰 연산이 필요한거 같음 Mind에 카운트 수를 두는게 나아보임
    }

    public List<FindMindOutput> findMinds() {
        return mindRepository.findAll()
                .stream()
                .map(a -> FindMindOutput.of(a, joinedMindService.countJoinedMindUser(a.getMindId())))
                .toList();
    }


    public Mind findVerifiedMind(Long mindId) {
        Optional<Mind> findMindById = mindRepository.findById(mindId);
        return findMindById.orElseThrow(() ->
                new RuntimeException("존재하지 않는 작심 번호입니다."));
    }
}
