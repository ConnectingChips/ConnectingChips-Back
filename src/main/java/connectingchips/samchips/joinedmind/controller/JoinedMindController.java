package connectingchips.samchips.joinedmind.controller;

import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import connectingchips.samchips.joinedmind.service.JoinedMindService;
import connectingchips.samchips.user.domain.LoginUser;
import connectingchips.samchips.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/joined-minds")
@RequiredArgsConstructor
public class JoinedMindController {

    private final JoinedMindService joinedMindService;

    @GetMapping("/{joined-mind-id}/join-check") //참여 여부
    public DataResponse joinCheck(@PathVariable("joined-mind-id")Long joinedMindId){
        return DataResponse.of(joinedMindService.JoinCheck(joinedMindId));
    }


    @PostMapping("/{mind-id}")
    @PreAuthorize("hasAnyRole('USER')")
    public BasicResponse joinMind(@PathVariable("mind-id")Long mindId,
                                  @LoginUser User loginUser){
        joinedMindService.makeMindRelation(mindId,loginUser);
        return BasicResponse.of(HttpStatus.CREATED);
    }
    @PutMapping("/{joined-mind-id}/exit")
    @PreAuthorize("hasAnyRole('USER')")
    public BasicResponse exitMind(@PathVariable("joined-mind-id")Long joinedMindId,
                                  @LoginUser User loginUser){
        joinedMindService.exitMindRelation(joinedMindId,loginUser.getId());
        return BasicResponse.of(HttpStatus.OK);
    }
    @PutMapping("/{joined-mind-id}/remind/{user-id}")
    @PreAuthorize("hasAnyRole('USER')")
    public BasicResponse reMind(@PathVariable("joined-mind-id")Long joinedMindId,
                                @LoginUser User loginUser){
        joinedMindService.reMindRelation(joinedMindId,loginUser.getId());
        return BasicResponse.of(HttpStatus.OK);
    }

}
