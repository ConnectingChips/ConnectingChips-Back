package connectingchips.samchips.mind.mindtype.controller;

import connectingchips.samchips.global.commons.dto.BasicResponse;
import connectingchips.samchips.global.commons.dto.DataResponse;
import connectingchips.samchips.mind.mindtype.dto.CreateMindTypeRequest;
import connectingchips.samchips.mind.mindtype.dto.UpdateMindTypeRequest;
import connectingchips.samchips.mind.mindtype.service.MindTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mind-types")
@RequiredArgsConstructor
public class MindTypeController {

    private final MindTypeService mindTypeService;

    /* 작심 종류 생성하기 */
    @PostMapping
    public DataResponse postMindType(@RequestBody CreateMindTypeRequest createMindTypeRequest){
        return DataResponse.of(HttpStatus.CREATED,mindTypeService.createMindType(createMindTypeRequest));
    }

    /* 작심 종류 조회하기 */
    @GetMapping("/{mind-type-id}")
    public DataResponse getMindType(@PathVariable("mind-type-id")Long mindTypeId){
        return DataResponse.of(mindTypeService.findMindType(mindTypeId));
    }

    /* 작심 종류 수정하기 */
    @PutMapping("/{mind-type-id}")
    public DataResponse putMindType(@PathVariable("mind-type-id")Long mindTypeId,
                                    @RequestBody UpdateMindTypeRequest updateMindTypeRequest){
        updateMindTypeRequest.setMindTypeId(mindTypeId);
        return DataResponse.of(mindTypeService.updateMindType(mindTypeId,updateMindTypeRequest));
    }

    /* 작심 종류 삭제하기 */
    @DeleteMapping("/{mind-type-id}")
    public BasicResponse deleteMindType(@PathVariable("mind-type-id")Long mindTypeId){
        mindTypeService.deleteMindType(mindTypeId);
        return BasicResponse.of(HttpStatus.OK);
    }
}
