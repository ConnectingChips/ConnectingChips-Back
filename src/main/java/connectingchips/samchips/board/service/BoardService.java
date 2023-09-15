package connectingchips.samchips.board.service;

import connectingchips.samchips.board.dto.BoardRequestDto;
import connectingchips.samchips.board.dto.BoardResponseDto;
import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.board.repository.BoardRepository;
import connectingchips.samchips.joinedmind.entity.JoinedMind;
import connectingchips.samchips.joinedmind.repository.JoinedMindRepository;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.repository.MindRepository;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.dto.UserRequestDto;
import connectingchips.samchips.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    public static final int JOINING = 1;
    private final BoardRepository boardRepository;
    private final MindRepository mindRepository;
    private final UserRepository userRepository;
    private final JoinedMindRepository joinedMindRepository;
    public void readBoardList(Long mindId) {
    }

    public BoardResponseDto.CanEdit isUserEditer(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));
        if(userId == board.getUser().getId()) {
            return new BoardResponseDto.CanEdit(true);
        } else return new BoardResponseDto.CanEdit(false);
    }

    @Transactional
    public void createBoard(BoardRequestDto boardRequestDto) {
        Mind mind = mindRepository.getReferenceById(boardRequestDto.getMindId());
        User user = userRepository.getReferenceById(boardRequestDto.getUserId());
        joinedMindRepository.save(checkJoinMind(user, mind).setIsJoining(JOINING));
        Board board = Board.builder()
                .content(boardRequestDto.getContent())
                .image(boardRequestDto.getImage())
                .mind(mind)
                .user(user)
                .build();
        boardRepository.save(board);
    }

    private static JoinedMind checkJoinMind(User user, Mind mind) {
        Optional<JoinedMind> first = user.getJoinedMinds().stream().filter(joinedMind -> Objects.equals(joinedMind.getMind().getMindId(), mind.getMindId()))
                .findFirst();
        JoinedMind joinedMind = first.orElseThrow(() -> new RuntimeException("작심에 참여하고 있지 않아 글을 쓸 수 없습니다."));
        if(joinedMind.getTodayWrite()) throw new RuntimeException("오늘 해당 게시글을 작성했습니다");
        return joinedMind;

    }

    @Transactional
    public void editInfo(Long userId, UserRequestDto.Edit editDto){
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

        findUser.editInfo(editDto.getNickname());
    }

    @Transactional
    public BoardResponseDto.Update updateBoard(Long boardId, BoardRequestDto boardRequestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 게시물입니다"));
        board.editContent(boardRequestDto.getContent());

        return new BoardResponseDto.Update(boardId, board.getContent());
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        if(boardRepository.existsById(boardId)) {
            boardRepository.deleteById(boardId);
        }
    }
}
