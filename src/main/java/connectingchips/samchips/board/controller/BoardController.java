package connectingchips.samchips.board.controller;

import connectingchips.samchips.board.dto.BoardRequestDto;
import connectingchips.samchips.board.dto.BoardResponseDto;
import connectingchips.samchips.board.service.BoardService;
import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    /* 게시글 리스트 반환 */
    @GetMapping("/{mind_id}")
    private BasicResponse getBoard(@PathVariable Long mindId){

        return BasicResponse.of(HttpStatus.OK);
    }
    /* 게시글 작성자 여부 */
    @GetMapping("/{board_id}/authentication")
    private BasicResponse getBoardUser(@PathVariable Long mindId){
        return BasicResponse.of(HttpStatus.OK);
    }

    /* 게시글 작성 */
    @PostMapping("/")
    private DataResponse<BoardResponseDto> createBoard(@RequestBody BoardRequestDto boardRequestDto) {
        BoardResponseDto boardResponseDto = new BoardResponseDto();
        return DataResponse.of(boardResponseDto);
    }

    /* 게시글 수정 */
    @PutMapping("/{board_id}")
    private BasicResponse updateBoard(@RequestBody BoardRequestDto boardRequestDto) {

        return BasicResponse.of(HttpStatus.OK);
    }

    /* 게시글 삭제 */
    @DeleteMapping("/{board_id}")
    private BasicResponse deleteComment(@PathVariable Long commentId){

        return BasicResponse.of(HttpStatus.OK);
    }
}
