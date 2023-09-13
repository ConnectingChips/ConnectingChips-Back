package connectingchips.samchips.joinedmind.controller;

import connectingchips.samchips.joinedmind.service.JoinedMindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/joined-minds")
@RequiredArgsConstructor
public class JoinedMindController {

    private final JoinedMindService joinedMindService;

    @GetMapping("/{joined-mind-id}/join-check")
    public ResponseEntity joinCheck(@PathVariable("joined-mind-id")Long joinedMindId){
        return new ResponseEntity(joinedMindService.JoinCheck(joinedMindId), HttpStatus.OK);
    }

    @PostMapping("/{mind-id}/{user-id}")
    public ResponseEntity joinMind(@PathVariable("mind-id")Long mindId,
                                   @PathVariable("user-id")Long userId){
        joinedMindService.makeMindRelation(mindId,userId);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    @PutMapping("/{joined-mind-id}/exit/{user-id}")
    public ResponseEntity exitMind(@PathVariable("joined-mind-id")Long joinedMindId,
                                   @PathVariable("user-id")Long userId){
        joinedMindService.exitMindRelation(joinedMindId,userId);
        return new ResponseEntity(HttpStatus.OK);
    }
    @PutMapping("/{joined-mind-id}/remind/{user-id}")
    public ResponseEntity reMind(@PathVariable("joined-mind-id")Long joinedMindId,
                                   @PathVariable("user-id")Long userId){
        joinedMindService.reMindRelation(joinedMindId,userId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
