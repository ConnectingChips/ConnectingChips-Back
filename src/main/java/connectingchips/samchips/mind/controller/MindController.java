package connectingchips.samchips.mind.controller;

import connectingchips.samchips.global.commons.dto.BasicResponse;
import connectingchips.samchips.global.commons.dto.DataResponse;
import connectingchips.samchips.mind.dto.request.CreateMindRequest;
import connectingchips.samchips.mind.dto.request.UpdateMindRequest;
import connectingchips.samchips.mind.dto.response.FindIntroMindResponse;
import connectingchips.samchips.mind.dto.response.FindPageMindResponse;
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

    /* 예시 이미지 반환*/
    @GetMapping("/upload/{mind-id}/image")
    public DataResponse getExampleImage(@PathVariable("mind-id")Long mindId){
        return DataResponse.of(mindService.getExampleImage(mindId));
    }

    /* 작심 정보 반환, 토큰 유무에 따라 todayWrite값 변환(0,1(회원),-1(비회원)) */
    @GetMapping("/intro/{mind-id}")
    public DataResponse getIntroMind(@PathVariable("mind-id")Long mindId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = mindService.getUsernameByAuthentication(auth);

        FindIntroMindResponse mind;
        if(Objects.equals(auth.getPrincipal().toString(), ANONYMOUS_USER))
            mind = mindService.findIntroMindNotUsername(mindId);
        else
            mind = mindService.findIntroMindByUsername(mindId, username);
        return DataResponse.of(mind);
    }

    /* 작심 정보 이미지 반환*/
    @GetMapping("/intro/{mind-id}/image")
    public DataResponse getIntroMindImage(@PathVariable("mind-id")Long mindId){

        return DataResponse.of(mindService.findIntroMindImage(mindId));
    }

    /*그룹페이지 정보 반환, 토큰 유무에 따라 todayWrite값 변환(0,1(회원),-1(비회원))*/
    @GetMapping("/page/{mind-id}")
    public DataResponse getPageMind(@PathVariable("mind-id")Long mindId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = mindService.getUsernameByAuthentication(auth);
        FindPageMindResponse minds;

        if(Objects.equals(auth.getPrincipal().toString(), ANONYMOUS_USER)) minds = mindService.findPageMindByAnonymous(mindId);
        else minds = mindService.findPageMindByUser(mindId, username);

        return DataResponse.of(minds);
    }

    /* 그룹 페이지 이미지 반환*/
    @GetMapping("/page/{mind-id}/image")
    public DataResponse getPageMindImage(@PathVariable("mind-id")Long mindId){
        return DataResponse.of(mindService.findPageMindImage(mindId));
    }

    /*액세스 토큰이 있을 경우 내가 가입한 작심 정보를 제외한 모든 작심 데이터 반환
      없을 경우 모든 작심 데이터 반환*/
    @GetMapping("/except-me")
    public DataResponse getAllMindExceptMe(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = mindService.getUsernameByAuthentication(auth);

        List<FindTotalMindResponse> minds;
        if(Objects.equals(auth.getPrincipal().toString(), ANONYMOUS_USER))
            minds = mindService.findAllMindExceptMe();
        else minds = mindService.findAllMindExceptMe(username);
        return DataResponse.of(minds);
    }

    /*액세스 토큰이 있을 경우 내가 가입한 작심 정보를 제외한 요청 작심 종류와 일치하는 작심 데이터 반환
      없을 경우 요청 작심 종류와 일치하는 작심 데이터 반환*/
    @GetMapping("/except-me/{mind-type-name}")
    public DataResponse getAllMindExceptMeByMindType(@PathVariable("mind-type-name")Long mindTypeId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = mindService.getUsernameByAuthentication(auth);

        List<FindTotalMindResponse> minds;
        if(Objects.equals(auth.getPrincipal().toString(), ANONYMOUS_USER))
            minds = mindService.findAllMindExceptMeByMindType(mindTypeId);
        else minds = mindService.findAllMindExceptMeByMindType(username, mindTypeId);

        return DataResponse.of(minds);
    }

    /* 당일 전체 참여한 작심의 id와 인증 여부(todayWrite(isDoneToday))를 반환 */
    @GetMapping("/today-check")
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse todaysAllCheck(@LoginUser User loginUser){
        return DataResponse.of(mindService.checkTodayAll(loginUser.getId()));
    }

    /* 유저가 참여하고 있는 (joinedMind의 isJoining이 1인) 작심들을 반환*/
    @GetMapping("/my-list")
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse getMyJoinMindList(@LoginUser User loginUser){
        return DataResponse.of(mindService.findMyJoinMindList(loginUser));
    }

    /* 유저가 참여하고 있는 (joinedMind의 isJoining이 1인) 작심을 반환 */
    @GetMapping("/my-list/{mind-id}")
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse getMyJoinMind(@LoginUser User loginUser,
                                      @PathVariable("mind-id") Long mindId){
        return DataResponse.of(mindService.findMyJoinMind(loginUser, mindId));
    }

    /* 유저가 참여했었던(joinedMind의 isJoining이 0인) 작심들을 반환*/
    @GetMapping("/my-joined-mind-list")
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse getMyJoinedMindList(@LoginUser User loginUser){
        return DataResponse.of(mindService.findMyJoinedMindList(loginUser));
    }

    /* 유저가 참여하고 있는 작심의 오늘 작심 완수여부 (todayWrite(isDoneToday))와 작심 달성여부(keepJoin)을 반환*/
    @GetMapping("/keep-join/{mind-id}")
    @PreAuthorize("hasAnyRole('USER')")
    public DataResponse checkKeepJoin(@LoginUser User loginUser,
                                      @PathVariable("mind-id")Long mindId){
        return DataResponse.of(mindService.checkReMindAvailability(loginUser,mindId));
    }

    /* 유저가 참여하고 있는 작심(JoinedMind)의 todayWrite를 false로 변경  */
    @PutMapping("/change-is-done-today/{mind-id}")
    public DataResponse changeIsDoneToday(@PathVariable("mind-id") Long mindId,
                                          @LoginUser User user) {
        return DataResponse.of(mindService.changeIsDoneToday(mindId,user));
    }

    /* 작심 생성하기 */
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

    /* 작심 수정하기 */
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

    /* 작심 삭제하기 */
    @DeleteMapping("/{mind-id}")
    public BasicResponse deleteMind(@PathVariable("mind-id")Long mindId){
        mindService.deleteMind(mindId);
        return BasicResponse.of(HttpStatus.OK);
    }
}

