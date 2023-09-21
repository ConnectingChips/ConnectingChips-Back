package connectingchips.samchips.joinedmind.controller;

import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import connectingchips.samchips.joinedmind.service.JoinedMindService;
import connectingchips.samchips.user.domain.LoginUser;
import connectingchips.samchips.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/joined-minds")
@RequiredArgsConstructor
@Slf4j
public class JoinedMindController {

    private final JoinedMindService joinedMindService;

    @GetMapping("/{joined-mind-id}/join-check") //참여 여부.....////////////마인드아이디쓰도록변경
    public DataResponse joinCheck(@PathVariable("joined-mind-id")Long joinedMindId){
        return DataResponse.of(joinedMindService.JoinCheck(joinedMindId));
    }



    @PostMapping("/{mind-id}")
    @PreAuthorize("hasAnyRole('USER')")
    public BasicResponse joinMind(@PathVariable("mind-id")Long mindId, @LoginUser User loginUser) {
        joinedMindService.makeMindRelation(mindId,loginUser);
        return BasicResponse.of(HttpStatus.CREATED);
    }
    @PutMapping("/{joined-mind-id}/exit")
    @PreAuthorize("hasAnyRole('USER')")
    public BasicResponse exitMind(@PathVariable("joined-mind-id")Long joinedMindId,
                                  @LoginUser User loginUser){
        joinedMindService.exitMindRelation(joinedMindId,loginUser);
        return BasicResponse.of(HttpStatus.OK);
    }
    @PutMapping("/{joined-mind-id}/remind")
    @PreAuthorize("hasAnyRole('USER')")
    public BasicResponse reMind(@PathVariable("joined-mind-id")Long joinedMindId,
                                @LoginUser User loginUser){
        joinedMindService.reMindRelation(joinedMindId,loginUser);
        return BasicResponse.of(HttpStatus.OK); //
    }

}
