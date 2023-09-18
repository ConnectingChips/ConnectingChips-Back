package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.joinedmind.entity.JoinedMind;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mindtype.entity.MindType;
import connectingchips.samchips.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyMindResponse {

    private final Long mindId;
    private final String mindTypeName;
    private final String name;
    private final Integer count;
    private final Integer boardCount;
    private final String myListImage;
    private final Boolean isDoneToday;

    public static MyMindResponse of(Mind mind, Integer count, Integer boardCount,Boolean isDoneToday) {
        return new MyMindResponse(
                mind.getMindId(),
                mind.getMindType().getName(),
                mind.getName(),
                count,
                boardCount,
                mind.getMyListImage(),
                isDoneToday
        );
    }
}
