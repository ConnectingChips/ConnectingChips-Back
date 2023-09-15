package connectingchips.samchips.mind.controller;

import connectingchips.samchips.joinedmind.entity.JoinedMind;
import connectingchips.samchips.mind.dto.controller.CreateMindInput;
import connectingchips.samchips.mind.dto.service.FindMindOutput;
import connectingchips.samchips.mind.service.MindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/minds")
@RequiredArgsConstructor
public class MindController {

    private final MindService mindService;


    @GetMapping("/{mind-id}")
    public ResponseEntity getMind(@PathVariable("mind-id")Long mindId){
        FindMindOutput mind = mindService.findMind(mindId);
        return new ResponseEntity(mind, HttpStatus.OK);
    }

//    @GetMapping("/today-check/{user-id}")
//    public ResponseEntity todaysAllCheck(@PathVariable("uesr-id") Long userId){
//        mindService.checkTodayAll(userId)

//    }
@GetMapping("/today-check/{joined-mind-id}/{user-id}")
public ResponseEntity todayCheck(@PathVariable("user-id")Long userId,
                                 @PathVariable("joined-mind-id")Long joinedMindId){
    return new ResponseEntity(mindService.checkToday(userId, joinedMindId), HttpStatus.OK);
}

    @GetMapping()
    public ResponseEntity getMinds(){
        return new ResponseEntity(mindService.findMinds(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity postMind(@RequestBody CreateMindInput createMindInput){
        mindService.createMind(createMindInput);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/{mind-id}")
    public ResponseEntity deleteMind(@PathVariable("mind-id")Long mindId){
        mindService.deleteMind(mindId);
        return new ResponseEntity(HttpStatus.OK);
    }

}
