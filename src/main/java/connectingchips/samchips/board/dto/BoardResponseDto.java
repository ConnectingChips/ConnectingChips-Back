package connectingchips.samchips.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardResponseDto {

    private Long boardId;
    private String content;


    public static class CanEdit {
        private boolean canEdit;

        public CanEdit(boolean isUserEditer) {
            this.canEdit = isUserEditer;
        }
    }
}
