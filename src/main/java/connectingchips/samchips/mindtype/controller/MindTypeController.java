package connectingchips.samchips.mindtype.controller;

import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import connectingchips.samchips.mindtype.dto.CreateMindTypeRequest;
import connectingchips.samchips.mindtype.service.MindTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mind-types")
@RequiredArgsConstructor
public class MindTypeController {

    private final MindTypeService mindTypeService;

    @PostMapping
    public BasicResponse postMindType(@RequestBody CreateMindTypeRequest createMindTypeRequest){
        mindTypeService.createMindType(createMindTypeRequest);
        return BasicResponse.of(HttpStatus.CREATED);
    }
    @GetMapping("/{mind-type-id}")
    public DataResponse getMindType(@PathVariable("mind-type-id")Long mindTypeId){
        return DataResponse.of(mindTypeService.findMindType(mindTypeId));
    }


    @DeleteMapping("/{mind-type-id}")
    public BasicResponse deleteMindType(@PathVariable("mind-type-id")Long mindTypeId){
        mindTypeService.deleteMindType(mindTypeId);
        return BasicResponse.of(HttpStatus.OK);
    }
}
