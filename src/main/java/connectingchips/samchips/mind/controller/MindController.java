package connectingchips.samchips.mind.controller;

import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import connectingchips.samchips.mind.dto.request.CreateMindRequest;
import connectingchips.samchips.mind.dto.response.FindMindResponse;
import connectingchips.samchips.mind.service.MindService;
import connectingchips.samchips.user.domain.LoginUser;
import connectingchips.samchips.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/minds")
@RequiredArgsConstructor
public class MindController {

    private final MindService mindService;


    @GetMapping("/{mind-id}")
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse getMind(@PathVariable("mind-id")Long mindId,
                                @LoginUser User user){
        FindMindResponse mind = mindService.findMind(mindId,user);
        return DataResponse.of(mind);
    }

    @GetMapping("/today-check")
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse todaysAllCheck(@LoginUser User loginUser){
        return DataResponse.of(mindService.checkTodayAll(loginUser.getId()));

    }
    @GetMapping("/today-check/{joined-mind-id}")
    public DataResponse todayCheck(@PathVariable("joined-mind-id")Long joinedMindId){
        return DataResponse.of(mindService.checkToday(joinedMindId));
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse getMinds(@LoginUser User user){
        return DataResponse.of(mindService.findMinds(user));
    }


    @GetMapping("/exceptMe")
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse getAllMindExceptMe(@LoginUser User loginUser){
        return DataResponse.of(mindService.findAllMindExceptMe(loginUser));
    }

    @GetMapping("/my-list")
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse getMyMindList(@LoginUser User loginUser){
        return DataResponse.of(mindService.findMyMindList(loginUser));
    }
    @PostMapping
    public BasicResponse postMind(@RequestBody CreateMindRequest createMindRequest){
        mindService.createMind(createMindRequest);

        return BasicResponse.of(HttpStatus.CREATED);
    }

    @DeleteMapping("/{mind-id}")
    public BasicResponse deleteMind(@PathVariable("mind-id")Long mindId){
        mindService.deleteMind(mindId);
        return BasicResponse.of(HttpStatus.OK);
    }

}
