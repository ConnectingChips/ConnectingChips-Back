package connectingchips.samchips.mindtype.controller;

import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import connectingchips.samchips.mindtype.dto.CreateMindTypeRequest;
import connectingchips.samchips.mindtype.dto.UpdateMindTypeRequest;
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
    public DataResponse postMindType(@RequestBody CreateMindTypeRequest createMindTypeRequest){
        return DataResponse.of(HttpStatus.CREATED,mindTypeService.createMindType(createMindTypeRequest));
    }
    @GetMapping("/{mind-type-id}")
    public DataResponse getMindType(@PathVariable("mind-type-id")Long mindTypeId){
        return DataResponse.of(mindTypeService.findMindType(mindTypeId));
    }

    @PutMapping("/{mind-type-id}")
    public DataResponse putMindType(@PathVariable("mind-type-id")Long mindTypeId,
                                    @RequestBody UpdateMindTypeRequest updateMindTypeRequest){
        updateMindTypeRequest.setMindTypeId(mindTypeId);
        return DataResponse.of(mindTypeService.updateMindType(mindTypeId,updateMindTypeRequest));
    }

    @DeleteMapping("/{mind-type-id}")
    public BasicResponse deleteMindType(@PathVariable("mind-type-id")Long mindTypeId){
        mindTypeService.deleteMindType(mindTypeId);
        return BasicResponse.of(HttpStatus.OK);
    }
}
