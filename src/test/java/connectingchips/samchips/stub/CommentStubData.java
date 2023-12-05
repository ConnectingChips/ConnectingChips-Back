package connectingchips.samchips.stub;

import connectingchips.samchips.board.dto.comment.CommentRequestDto;
import connectingchips.samchips.board.dto.comment.CommentResponseDto;
import connectingchips.samchips.board.entity.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentStubData {

    BoardStubData boardStubData;
    UserStubData userStubData;

    public CommentStubData(BoardStubData boardStubData, UserStubData userStubData) {
        this.boardStubData = boardStubData;
        this.userStubData = userStubData;
    }

    public Comment createComment() {
        return Comment.builder()
                .board(boardStubData.createBoard())
                .user(userStubData.createUser1())
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
