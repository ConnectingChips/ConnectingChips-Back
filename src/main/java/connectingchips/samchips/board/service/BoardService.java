package connectingchips.samchips.board.service;

import connectingchips.samchips.board.dto.BoardRequestDto;
import connectingchips.samchips.board.dto.BoardResponseDto;
import connectingchips.samchips.board.dto.UserEditDto;
import connectingchips.samchips.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    public void readBoardList(Long mindId) {
    }

    public UserEditDto isUserEdit(Long boardId) {
    }

    public void createBoard(BoardRequestDto boardRequestDto) {
    }

    public BoardResponseDto updateBoard(BoardRequestDto boardRequestDto) {
    }

    public void deleteBoard(Long boardId) {
        if(boardRepository.existsById(boardId)) {
            boardRepository.deleteById(boardId);
        }
    }
}
