package connectingchips.samchips.board.repository;

import connectingchips.samchips.board.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByCommentId(Long commentId);

    long countByCommentId(Long commentId);
}
