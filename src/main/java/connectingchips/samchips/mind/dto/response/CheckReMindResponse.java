package connectingchips.samchips.mind.dto.response;

import connectingchips.samchips.joinedmind.entity.JoinedMind;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CheckReMindResponse {
    private final Boolean keepJoin;
    private final Boolean isDoneToday;

    public static CheckReMindResponse of(JoinedMind joinedMind){
        return new CheckReMindResponse(
                joinedMind.getCount()==3,
                joinedMind.getTodayWrite()
        );
    }
    public static CheckReMindResponse of(){ //joinedMind가 없을 경우
        return new CheckReMindResponse(
                true,
                false
        );
    }

}
