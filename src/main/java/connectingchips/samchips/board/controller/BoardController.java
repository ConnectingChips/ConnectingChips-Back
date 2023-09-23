package connectingchips.samchips.board.controller;

import connectingchips.samchips.board.dto.BoardRequestDto;
import connectingchips.samchips.board.dto.BoardResponseDto;
import connectingchips.samchips.board.service.BoardService;
import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.commons.dto.DataResponse;
import connectingchips.samchips.utils.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /* 게시글 리스트 반환 */
    @GetMapping("/{mind_id}")
    public DataResponse<List<BoardResponseDto.Read>> getMindBoardList(@PathVariable(value = "mind_id") Long mindId){
        List<BoardResponseDto.Read> mindBoardList = boardService.getMindBoardList(mindId);
        return DataResponse.of(mindBoardList);
    }

    /* 게시글 작성자 여부 */
    @GetMapping("/authentication")
    public DataResponse<BoardResponseDto.CanEdit> getBoardUser(@RequestParam(value = "board_id") Long boardId,
                                                                @RequestParam(value = "user_id") Long userId){
        BoardResponseDto.CanEdit canEdit = boardService.isUserEditor(boardId, userId);
        return DataResponse.of(canEdit);
    }

    /* 게시글 작성 */
    @PostMapping
    @LogExecutionTime
    public BasicResponse createBoard(@RequestPart(value = "file", required = false) MultipartFile file,
                                      @RequestPart(value = "boardRequestDto") BoardRequestDto.Save boardRequestDto) throws IOException {
        boardService.createBoard(file, boardRequestDto);

        return BasicResponse.of(HttpStatus.OK);
    }

    /* 게시글 수정 */
    @PutMapping("/{board_id}")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.PUT) //특정 url에 cors에러 해결
    public DataResponse<BoardResponseDto.Update> updateBoard(@PathVariable(value = "board_id") Long boardId, @RequestBody BoardRequestDto.Edit boardRequestDto) {
        BoardResponseDto.Update boardResponseDto = boardService.updateBoard(boardId, boardRequestDto);
        return DataResponse.of(boardResponseDto);
    }

    /* 게시글 삭제 */
    @DeleteMapping("/{board_id}")
    public BasicResponse deleteBoard(@PathVariable(value = "board_id") Long boardId){
        boardService.deleteBoard(boardId);
        return BasicResponse.of(HttpStatus.OK);
    }
}
