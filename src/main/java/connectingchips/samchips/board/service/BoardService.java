package connectingchips.samchips.board.service;

import connectingchips.samchips.board.dto.BoardRequestDto;
import connectingchips.samchips.board.dto.BoardResponseDto;
import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.board.repository.BoardRepository;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.repository.MindRepository;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MindRepository mindRepository;
    private final UserRepository userRepository;
    public void readBoardList(Long mindId) {
    }

    public BoardResponseDto.CanEdit isUserEditer(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));
        if(userId == board.getUser().getId()) {
            return new BoardResponseDto.CanEdit(true);
        } else return new BoardResponseDto.CanEdit(false);
    }

    /**
     *     private Long mindId;
     *     private String content;
     *     private String image;
     */
    @Transactional
    public void createBoard(BoardRequestDto boardRequestDto) {
        Mind mind = mindRepository.getReferenceById(boardRequestDto.getMindId());
        User user = userRepository.getReferenceById(boardRequestDto.getUserId());
        Board board = Board.builder()
                .content(boardRequestDto.getContent())
                .image(boardRequestDto.getImage())
                .mind(mind)
                .user(user)
                .build();
        boardRepository.save(board);
    }

    public BoardResponseDto updateBoard(BoardRequestDto boardRequestDto) {
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        if(boardRepository.existsById(boardId)) {
            boardRepository.deleteById(boardId);
        }
    }
}
