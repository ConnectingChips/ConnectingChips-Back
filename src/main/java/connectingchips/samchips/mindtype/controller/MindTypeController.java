package connectingchips.samchips.mindtype.controller;


import connectingchips.samchips.mindtype.dto.CreateMindTypeInput;
import connectingchips.samchips.mindtype.service.MindTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mind-types")
@RequiredArgsConstructor
public class MindTypeController {

    private final MindTypeService mindTypeService;

    @PostMapping
    public ResponseEntity postMindType(@RequestBody CreateMindTypeInput createMindTypeInput){
        mindTypeService.createMindType(createMindTypeInput);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    @GetMapping("/{mind-type-id}")
    public ResponseEntity getMindType(@PathVariable("mind-type-id")Long mindTypeId){
        return new ResponseEntity(mindTypeService.findMindType(mindTypeId), HttpStatus.CREATED);
    }


    @DeleteMapping("/{mind-type-id}")
    public ResponseEntity deleteMindType(@PathVariable("mind-type-id")Long mindTypeId){
        mindTypeService.deleteMindType(mindTypeId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
