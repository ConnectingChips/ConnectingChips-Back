package connectingchips.samchips.board.repository;

import connectingchips.samchips.board.stub.BoardStubData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

//조금의 연구가 필요할듯
@ExtendWith(MockitoExtension.class)
class BoardRepositoryTest {

    private BoardStubData boardStubData;

    @InjectMocks
    BoardRepository boardRepository;

    @BeforeEach
    void init() {
        boardStubData = new BoardStubData();
    }
    @Test
    void 유저_작심게시글갯수_조회() {
//        //given
//        Mind mind = boardStubData.createMind();
//        User user = boardStubData.createUser();
//        //when
//        Integer result = boardRepository.findBoardCountByUserAndMind(user, mind);
//        //then
//        Assertions.assertThat(result).isEqualTo(0);
    }
}