package connectingchips.samchips.joinedmind.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JoinedMindRepositoryImpl implements JoinedMindRepositoryCustom{

    public static final int ALREADY_JOINING = 1;
    private final JPAQueryFactory queryFactory;

}
