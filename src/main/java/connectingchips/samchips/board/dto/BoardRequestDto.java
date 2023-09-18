package connectingchips.samchips.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {

    private Long mindId;
    private Long userId;
    private String content;

}
