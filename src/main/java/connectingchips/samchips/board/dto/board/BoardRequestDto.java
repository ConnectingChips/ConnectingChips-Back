package connectingchips.samchips.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Save{

        private Long mindId;
        private Long userId;
        private String content;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Edit{

        private String content;

    }
}
