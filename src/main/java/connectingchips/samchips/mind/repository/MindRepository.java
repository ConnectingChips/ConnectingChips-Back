package connectingchips.samchips.mind.repository;

import connectingchips.samchips.mind.entity.Mind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Scheduled;

public interface MindRepository extends JpaRepository<Mind,Long> {

}
