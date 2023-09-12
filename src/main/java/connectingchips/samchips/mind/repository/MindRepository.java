package connectingchips.samchips.mind.repository;

import connectingchips.samchips.mind.dto.service.FindMindOutput;
import connectingchips.samchips.mind.entity.Mind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MindRepository extends JpaRepository<Mind,Long> {


}
