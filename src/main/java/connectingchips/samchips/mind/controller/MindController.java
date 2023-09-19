package connectingchips.samchips.mind.controller;

import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import connectingchips.samchips.mind.dto.request.CreateMindRequest;
import connectingchips.samchips.mind.dto.request.UpdateMindRequest;
import connectingchips.samchips.mind.dto.response.FindIntroMindResponse;
import connectingchips.samchips.mind.dto.response.FindTotalMindResponse;
import connectingchips.samchips.mind.service.MindService;
import connectingchips.samchips.user.domain.LoginUser;
import connectingchips.samchips.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/minds")
@RequiredArgsConstructor
@Slf4j
public class MindController {

    public static final String ANONYMOUS_USER = "anonymousUser";
    private final MindService mindService;


    @GetMapping("/intro/{mind-id}")
    public DataResponse getIntroMind(@PathVariable("mind-id")Long mindId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        FindIntroMindResponse mind;
        if(Objects.equals(auth.getPrincipal().toString(), ANONYMOUS_USER))
            mind = mindService.findIntroMindNotAccountId(mindId);
        else
            mind = mindService.findIntroMindByAccountId(mindId,makeAccountId(auth));
        return DataResponse.of(mind);
    }
    @GetMapping("/intro/{mind-id}/image")
    public DataResponse getIntroMindImage(@PathVariable("mind-id")Long mindId){

        return DataResponse.of(mindService.findIntroMindImage(mindId));
    }

      @GetMapping("/page/{mind-id}")
      @PreAuthorize("hasAnyRole('USER')")
    public DataResponse getPageMind(@PathVariable("mind-id")Long mindId,
                                    @LoginUser User user){
        return DataResponse.of(mindService.findPageMind(mindId,user));
    }
    @GetMapping("/page/{mind-id}/image")
    public DataResponse getPageMindImage(@PathVariable("mind-id")Long mindId){
        return DataResponse.of(mindService.findPageMindImage(mindId));
    }
//    @GetMapping /except-me와 동작방식이 유사하나 혹시 몰라서 남겨둠
//    public DataResponse getTotalMind(){
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        List<FindTotalMindResponse> minds;
//        if(Objects.equals(auth.getPrincipal().toString(), ANONYMOUS_USER))
//            minds = mindService.findTotalMindNotAccountId();
//        else
//            minds = mindService.findTotalMindByAccountId(makeAccountId(auth));
//        return DataResponse.of(minds);
//    }

    @GetMapping("/except-me")
    public DataResponse getAllMindExceptMe(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<FindTotalMindResponse> minds;
        if(Objects.equals(auth.getPrincipal().toString(), ANONYMOUS_USER))
            minds = mindService.findAllMindExceptMe();
        else minds = mindService.findAllMindExceptMe(makeAccountId(auth));
        return DataResponse.of(minds);
    }
    @GetMapping("/except-me/{mind-type-name}")
    public DataResponse getAllMindExceptMeByMindType(@PathVariable("mind-type-name")Long mindTypeId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<FindTotalMindResponse> minds;
        if(Objects.equals(auth.getPrincipal().toString(), ANONYMOUS_USER))
            minds = mindService.findAllMindExceptMeByMindType(mindTypeId);
        else minds = mindService.findAllMindExceptMeByMindType(makeAccountId(auth),mindTypeId);

        return DataResponse.of(minds);
    }

    @GetMapping("/today-check")
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse todaysAllCheck(@LoginUser User loginUser){
        return DataResponse.of(mindService.checkTodayAll(loginUser.getId()));

    }
    @GetMapping("/today-check/{joined-mind-id}")
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse todayCheck(@PathVariable("joined-mind-id")Long joinedMindId){
        return DataResponse.of(mindService.checkToday(joinedMindId));
    }

    @GetMapping("/my-list")
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse getMyJoinMindList(@LoginUser User loginUser){
        return DataResponse.of(mindService.findMyJoinMindList(loginUser));
    }

    @GetMapping("/my-joined-mind-list")
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse getMyJoinedMindList(@LoginUser User loginUser){
        return DataResponse.of(mindService.findMyJoinedMindList(loginUser));
    }


    @PostMapping
    public BasicResponse postMind(
            @RequestPart CreateMindRequest createMindRequest,
                                 @RequestPart MultipartFile introImage,
                                 @RequestPart MultipartFile pageImage,
                                 @RequestPart MultipartFile totalListImage,
                                 @RequestPart MultipartFile myListImage) throws IOException {

        List<MultipartFile> images = List.of(introImage, pageImage, totalListImage, myListImage);
        return DataResponse.of(HttpStatus.CREATED, mindService.createMind(createMindRequest,images));
    }

    @PutMapping("/{mind-id}")
    public DataResponse putMind(@PathVariable("mind-id") Long mindId,
                                @RequestPart(required = false) UpdateMindRequest updateMindRequest,
                                @RequestPart MultipartFile introImage,
                                @RequestPart MultipartFile pageImage,
                                @RequestPart MultipartFile totalListImage,
                                @RequestPart MultipartFile myListImage) throws IOException {
        List<MultipartFile> images = List.of(introImage, pageImage, totalListImage, myListImage);
        return DataResponse.of(mindService.updateMind(mindId,updateMindRequest,images));
    }

    @DeleteMapping("/{mind-id}")
    public BasicResponse deleteMind(@PathVariable("mind-id")Long mindId){
        mindService.deleteMind(mindId);
        return BasicResponse.of(HttpStatus.OK);
    }


    private static String makeAccountId(Authentication auth) {
        String string = auth.getPrincipal().toString();
        Integer start = 0;
        Integer end = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '=') {
                start = i+1;
                break;
            }
        }
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ',') {
                end = i;
                break;
            }
        }
        return string.substring(start, end);
    }
}

