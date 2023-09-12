package connectingchips.samchips.joinedmind.repository;

import connectingchips.samchips.joinedmind.entity.JoinedMind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JoinedMindRepository extends JpaRepository<JoinedMind,Long> {

    @Query("select COUNT(*) from JoinedMind as j where j.mind.mindId = :id")
    Integer countJoinedMindUser(@Param("id")Long mindId);
}
