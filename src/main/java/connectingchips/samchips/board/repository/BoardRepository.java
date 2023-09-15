package connectingchips.samchips.board.repository;

import connectingchips.samchips.board.entity.Board;
import connectingchips.samchips.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {


    List<Board> findAllByCreatedAt_DayOfMonthAndUser(int createdAt_dayOfMonth, User user);
}
