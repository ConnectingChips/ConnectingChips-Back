package connectingchips.samchips.board.controller;

import connectingchips.samchips.board.dto.BoardRequestDto;
import connectingchips.samchips.board.dto.BoardResponseDto;
import connectingchips.samchips.board.service.BoardService;
import connectingchips.samchips.global.commons.dto.BasicResponse;
import connectingchips.samchips.global.commons.dto.DataResponse;
import connectingchips.samchips.user.domain.LoginUser;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

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
    @PreAuthorize("hasRole('USER')")
    public BasicResponse createBoard(@RequestPart(value = "file") MultipartFile file,
                                     @RequestPart(value = "boardRequestDto") BoardRequestDto.Save boardRequestDto) throws IOException {
        boardService.createBoard(file, boardRequestDto);
        return BasicResponse.of(HttpStatus.OK);
    }

    /* 게시글 수정 */
    @PutMapping("/{board_id}")
    @PreAuthorize("hasRole('USER')")
    public DataResponse<BoardResponseDto.Update> updateBoard(@PathVariable(value = "board_id") Long boardId,
                                                             @RequestBody BoardRequestDto.Edit boardRequestDto,
                                                             @LoginUser User loginUser) {
        userService.isLoginUser(loginUser, boardService.getBoardById(boardId).getUser());
        BoardResponseDto.Update boardResponseDto = boardService.updateBoard(boardId, boardRequestDto);
        return DataResponse.of(boardResponseDto);
    }

    /* 게시글 삭제 */
    @DeleteMapping("/{board_id}")
    @PreAuthorize("hasRole('USER')")
    public BasicResponse deleteBoard(@PathVariable(value = "board_id") Long boardId, @LoginUser User loginUser){
        userService.isLoginUser(loginUser, boardService.getBoardById(boardId).getUser());
        boardService.deleteBoard(boardId);
        return BasicResponse.of(HttpStatus.OK);
    }
}
