package connectingchips.samchips.mindtype.repository;

import connectingchips.samchips.mindtype.entity.MindType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MindTypeRepository extends JpaRepository<MindType, Long> {
}
