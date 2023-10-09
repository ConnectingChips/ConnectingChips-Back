package connectingchips.samchips.joinedmind.controller;

import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import connectingchips.samchips.joinedmind.service.JoinedMindService;
import connectingchips.samchips.user.domain.LoginUser;
import connectingchips.samchips.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/joined-minds")
@RequiredArgsConstructor
@Slf4j
public class JoinedMindController {

    private final JoinedMindService joinedMindService;

    /* 작심에 참여하고 있는지 확인여부 */
    @GetMapping("/{mind-id}/join-check")
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse joinCheck(@PathVariable("mind-id")Long mindId, @LoginUser User loginUser){
        return DataResponse.of(joinedMindService.JoinCheck(mindId,loginUser));
    }

    /* 유저를 작심에서 참여 */
    @PostMapping("/{mind-id}")
    @PreAuthorize("hasAnyRole('USER')")
    public BasicResponse createJoinedMind(@PathVariable("mind-id")Long mindId, @LoginUser User loginUser) {
        joinedMindService.makeMindRelation(mindId,loginUser);
        return BasicResponse.of(HttpStatus.CREATED);
    }

    /* 유저를 작심에서 탈퇴 */
    @PutMapping("/{mind-id}/exit")
    @PreAuthorize("hasAnyRole('USER')")
    public BasicResponse exitJoinedMind(@PathVariable("mind-id") Long mindId,
                                  @LoginUser User loginUser){
        joinedMindService.exitMindRelation(mindId,loginUser);
        return BasicResponse.of(HttpStatus.OK);
    }

    /* 작심을 완료한 후 재작심 하기 */
    @PutMapping("/{mind-id}/remind")
    @PreAuthorize("hasAnyRole('USER')")
    public BasicResponse updateJoinedMind(@PathVariable("mind-id") Long mindId,
                                @LoginUser User loginUser){
        joinedMindService.updateKeepJoin(mindId, loginUser);
        return BasicResponse.of(HttpStatus.OK);
    }

    /* 작심을 완료한 후 작심 초기 세팅하기 (keepJoin -> true, count -> 0) */
    @PutMapping("/changeKeepJoin")
    public BasicResponse changeKeepJoin(){
        joinedMindService.resetCountAndUpdateKeepJoin();
        return BasicResponse.of(HttpStatus.OK);
    }
}
