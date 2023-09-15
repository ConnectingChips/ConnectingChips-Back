package connectingchips.samchips.mind.controller;

import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import connectingchips.samchips.mind.dto.request.CreateMindRequest;
import connectingchips.samchips.mind.dto.response.FindMindResponse;
import connectingchips.samchips.mind.service.MindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/minds")
@RequiredArgsConstructor
public class MindController {

    private final MindService mindService;


    @GetMapping("/{mind-id}")
    public DataResponse getMind(@PathVariable("mind-id")Long mindId){
        FindMindResponse mind = mindService.findMind(mindId);
        return DataResponse.of(mind);
    }

    @GetMapping("/today-check/{user-id}")
    public DataResponse todaysAllCheck(@PathVariable("uesr-id") Long userId){
        return DataResponse.of(mindService.checkTodayAll(userId));

    }
@GetMapping("/today-check/{joined-mind-id}")
public DataResponse todayCheck(@PathVariable("joined-mind-id")Long joinedMindId){
    return DataResponse.of(mindService.checkToday(joinedMindId));
}

    @GetMapping()
    public DataResponse getMinds(){
        return DataResponse.of(mindService.findMinds());
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
