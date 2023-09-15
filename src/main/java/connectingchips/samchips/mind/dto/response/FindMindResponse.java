package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.mind.entity.Mind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindMindResponse {

    private final Long mindId;
    private final String mindType;
    private final String name;
    private final String introduce;
    private final Integer userCount;
    private final String writeFormat;
    private final Integer canJoin;
    private final String backgroundImage;

    public static FindMindResponse of(final Mind mind, final Integer joinedMindPeopleCount){
        return new FindMindResponse(
                mind.getMindId(),
                mind.getMindType().getName(),
                mind.getName(),
                mind.getIntroduce(),
                joinedMindPeopleCount,
                mind.getWriteFormat(),
                mind.getJoinedMind().getIsJoining(), //현재 참여한 작심에서 참가가능 여부를 가져오고있음, 만약 정원에 따른 참가여부라면다른 로직 필요
                mind.getBackgroundImage()
                );
    }
}
