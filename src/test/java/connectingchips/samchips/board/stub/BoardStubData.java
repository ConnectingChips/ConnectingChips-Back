package connectingchips.samchips.board.stub;

import connectingchips.samchips.board.comment.entity.Comment;
import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.joinedmind.entity.JoinedMind;
import connectingchips.samchips.mind.mindtype.entity.MindType;
import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class BoardStubData {

    public User createUser(){
        return  User.builder()
                .email("test@test.com")
                .password("test123456")
                .nickname("테스트닉네임")
                .accountId("test12345")
                .ageRange("17")
                .gender("남")
                .socialType(SocialType.SAMCHIPS)
                .profileImage("test.png")
                .build();
    }
    public Board createBoard() {
        User user = createUser();
        Mind mind = createMind();
        JoinedMind joinedMind = createJoinedMind(user, mind);
        List<JoinedMind> joinedMinds = user.getJoinedMinds();
        joinedMinds.add(joinedMind);
        user.editJoinedMinds(joinedMinds);

        return Board.builder()
                .mind(mind)
                .user(user)
                .content("게시판내용")
                .image("게시판이미지.jpg")
                .build();
    }

    private static JoinedMind createJoinedMind(User user, Mind mind) {
        return JoinedMind.builder()
                .user(user)
                .mind(mind)
                .build();
    }

    private MindType createMindType() {
       return MindType.builder()
                .name("작심타입테스트")
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
    public Mind createMind(){
        MindType mindtype = createMindType();
        Mind mind = Mind.builder()
                .name("작심테스트")
                .mindType(mindtype)
                .writeFormat("작성방식예제")
                .introduce("소개 예제")
                .myListImage("mylist.jpg")
                .pageImage("page.jpg")
                .totalListImage("totallist.jpg")
                .introImage("intro.jpg")
                .exampleImage("example.jpg")
                .build();
        return mind;
    }

}