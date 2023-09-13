package connectingchips.samchips.joinedmind.service;

import connectingchips.samchips.joinedmind.dto.service.JoinCheckOutPut;
import connectingchips.samchips.joinedmind.repository.JoinedMindRepository;
import connectingchips.samchips.mind.service.MindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinedMindService {

    private final JoinedMindRepository joinedMindRepository;


    public JoinCheckOutPut JoinCheck(Long mindId) {

        return null;
    }


    public Integer countJoinedMindUser(Long joinedMindId){
        return joinedMindRepository.countJoinedMindUser(joinedMindId);
    }

//    private JoinedMind findVerifiedJoinedMind(Long joinedMindId) {
//        Optional<JoinedMind> findJoinedMindById = joinedMindRepository.findById(mindId);
//        return findJoinedMindById.orElseThrow(() ->
//                new RuntimeException("존재하지 않는 참여한 작심 번호입니다."));
//    } 미구현
}
