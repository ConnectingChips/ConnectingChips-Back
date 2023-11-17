package connectingchips.samchips.mind.repository;

import connectingchips.samchips.mind.entity.JoinedMind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JoinedMindRepository extends JpaRepository<JoinedMind,Long> ,JoinedMindRepositoryCustom{

    @Query("select COUNT(*) from JoinedMind as j where j.mind.mindId = :id")
    Integer countJoinedMindUser(@Param("id")Long mindId);
}
