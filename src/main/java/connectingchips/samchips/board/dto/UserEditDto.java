package connectingchips.samchips.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserEditDto {

    boolean canEdit;

    public UserEditDto(boolean isUserEditer) {
        this.canEdit = isUserEditer;
    }
}
