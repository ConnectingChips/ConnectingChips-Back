package connectingchips.samchips.board.controller;

import connectingchips.samchips.board.dto.BoardRequestDto;
import connectingchips.samchips.board.dto.BoardResponseDto;
import connectingchips.samchips.board.dto.UserEditDto;
import connectingchips.samchips.board.service.BoardService;
import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    /* 게시글 리스트 반환 */
    @GetMapping("/{mind_id}")
    private DataResponse<?> getBoard(@PathVariable Long mindId){
        boardService.readBoardList(mindId);
        return DataResponse.of();
    }

    /* 게시글 작성자 여부 */

    /**
     * @GetMapping({"board", "board?page={page}&listSize={listSize}"})
     * 	public String getBoardList(Model model
     * 			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
     * 			, @RequestParam(value = "listSize", defaultValue = "10") int listSize
     * 			) throws Exception {
     * @return
     */
    @GetMapping("/boards/authentication?board_id={board_id}&user_id={user_id} ")
    private DataResponse<BoardResponseDto.CanEdit> getBoardUser(@RequestParam(value = "board_id") Long boardId,
                                                                @RequestParam(value = "user_id") Long userId){
        BoardResponseDto.CanEdit canEdit = boardService.isUserEditer(boardId, userId);
        return DataResponse.of(canEdit);
    }

    /* 게시글 작성 */
    @PostMapping("/")
    private BasicResponse createBoard(@RequestBody BoardRequestDto boardRequestDto) {
        boardService.createBoard(boardRequestDto);
        return BasicResponse.of(HttpStatus.OK);
    }

    /* 게시글 수정 */
    @PutMapping("/{board_id}")
    private DataResponse<BoardResponseDto> updateBoard(@RequestBody BoardRequestDto boardRequestDto) {
        BoardResponseDto boardResponseDto = boardService.updateBoard(boardRequestDto);
        return DataResponse.of(boardResponseDto);
    }

    /* 게시글 삭제 */
    @DeleteMapping("/{board_id}")
    private BasicResponse deleteBoard(@PathVariable Long boardId){
        boardService.deleteBoard(boardId);
        return BasicResponse.of(HttpStatus.OK);
    }
}
