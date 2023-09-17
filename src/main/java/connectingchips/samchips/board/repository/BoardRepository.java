package connectingchips.samchips.board.repository;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.mind.entity.Mind;
import connectingchips.samchips.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByMind(Mind mind);

    @Query("select COUNT(*) FROM Board b WHERE b.user = :user AND b.mind = :mind")
    Integer findBoardCountByUserAndMind(User user, Mind mind);
}
