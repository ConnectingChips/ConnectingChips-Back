package connectingchips.samchips.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {
    /**
     *     "mindId": 2,
     *     "content": "내용",
     *     "image": "인증 이미지"
     */
    private Long mindId;
    private Long userId;
    private String content;
    private String image;

}
