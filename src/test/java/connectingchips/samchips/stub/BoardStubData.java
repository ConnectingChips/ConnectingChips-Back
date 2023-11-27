package connectingchips.samchips.stub;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.entity.JoinedMind;
import connectingchips.samchips.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class BoardStubData {

    UserStubData userStubData = new UserStubData();
    MindStubData mindStubData = new MindStubData();

    public Board createBoard() {
        User user = userStubData.createUser1();
        Mind mind = mindStubData.createMind();
        JoinedMind joinedMind = mindStubData.createJoinedMind(user, mind);
        List<JoinedMind> joinedMinds = user.getJoinedMinds();
        joinedMinds.add(joinedMind);
        user.updateJoinedMinds(joinedMinds);

        return Board.builder()
                .mind(mind)
                .user(user)
                .content("게시판내용")
                .image("게시판이미지.jpg")
                .build();
    }

    public List<Board> createBoards() {
        List<Board> boards = new ArrayList<>();

        Board board1 = createBoard();
        Board board2 = createBoard();
        board2.updateContent("수정된 게시판 내용");
        boards.add(board1);
        boards.add(board2);

        return boards;
    }
}