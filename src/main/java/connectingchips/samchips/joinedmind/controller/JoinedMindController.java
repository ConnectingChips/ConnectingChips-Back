package connectingchips.samchips.joinedmind.controller;

import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import connectingchips.samchips.joinedmind.service.JoinedMindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/{mind-id}/{user-id}")
    public BasicResponse joinMind(@PathVariable("mind-id")Long mindId,
                                  @PathVariable("user-id")Long userId){
        joinedMindService.makeMindRelation(mindId,userId);
        return BasicResponse.of(HttpStatus.CREATED);
    }
    @PutMapping("/{joined-mind-id}/exit/{user-id}")
    public BasicResponse exitMind(@PathVariable("joined-mind-id")Long joinedMindId,
                                   @PathVariable("user-id")Long userId){
        joinedMindService.exitMindRelation(joinedMindId,userId);
        return BasicResponse.of(HttpStatus.OK);
    }
    @PutMapping("/{joined-mind-id}/remind/{user-id}")
    public BasicResponse reMind(@PathVariable("joined-mind-id")Long joinedMindId,
                                   @PathVariable("user-id")Long userId){
        joinedMindService.reMindRelation(joinedMindId,userId);
        return BasicResponse.of(HttpStatus.OK);
    }
}
