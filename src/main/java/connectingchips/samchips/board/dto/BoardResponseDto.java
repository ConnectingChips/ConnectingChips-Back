package connectingchips.samchips.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardResponseDto {

    public static class Update{
        private Long boardId;
        private String content;

        public Update(Long boardId, String content){
            this.boardId = boardId;
            this.content = content;
        }
    }

    public static class CanEdit {
        private boolean canEdit;

        public CanEdit(boolean isUserEditer) {
            this.canEdit = isUserEditer;
        }
    }
}
