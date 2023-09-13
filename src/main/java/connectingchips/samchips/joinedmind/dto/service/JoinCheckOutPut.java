package connectingchips.samchips.joinedmind.dto.service;

import connectingchips.samchips.joinedmind.entity.JoinedMind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JoinCheckOutPut {

    private final Boolean isJoining;

    public static JoinCheckOutPut of(Integer isJoining){
        return new JoinCheckOutPut(isJoining == 1);
        //Boolean을 반환하기 위해 서비스계층에서 불리언값을 주는게 맞을지 고민
    }
}
