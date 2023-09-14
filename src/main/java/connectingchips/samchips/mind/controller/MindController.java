package connectingchips.samchips.mind.controller;

import connectingchips.samchips.mind.dto.service.FindMindOutput;
import connectingchips.samchips.mind.service.MindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping()
    public ResponseEntity getMinds(){
        return new ResponseEntity(mindService.findMinds(), HttpStatus.OK);
    }
}
