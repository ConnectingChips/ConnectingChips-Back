package connectingchips.samchips.board.repository;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBoard(Board board);
    long countByBoard(Board board);
}
