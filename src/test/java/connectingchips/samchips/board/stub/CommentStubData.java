package connectingchips.samchips.board.stub;

import connectingchips.samchips.board.comment.dto.CommentRequestDto;
import connectingchips.samchips.board.comment.dto.CommentResponseDto;
import connectingchips.samchips.board.comment.entity.Comment;
import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.mind.joinedmind.entity.JoinedMind;
import connectingchips.samchips.mind.mindtype.entity.MindType;
import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class CommentStubData {

    BoardStubData boardStubData;

    public CommentStubData(BoardStubData boardStubData) {
        this.boardStubData = boardStubData;
    }

    public Comment createComment() {
        return Comment.builder()
                .board(boardStubData.createBoard())
                .user(boardStubData.createUser())
                .content("댓글 테스트1")
                .build();
    }

    public List<Comment> createComments(){
        Comment comment1 = createComment();
        Comment comment2 = createComment();
        List<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);
        return comments;
    }

    public CommentRequestDto createCommentRequestDto() {

        return CommentRequestDto.builder()
                .boardId(1L)
                .userId(1L)
                .content("commentRequestDto 테스트")
                .build();
    }

    public CommentResponseDto.Read createCommentResponseDtoRead() {

        return new CommentResponseDto.Read(createComment());
    }
}
