package connectingchips.samchips.joinedmind.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import connectingchips.samchips.joinedmind.entity.QJoinedMind;
import connectingchips.samchips.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static connectingchips.samchips.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JoinedMindRepositoryImpl implements JoinedMindRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 0시 0분 0초에 실행
    public void resetTodayWrite(){
        QJoinedMind joinedMind = QJoinedMind.joinedMind;
        queryFactory.update(joinedMind)
                        .set(joinedMind.todayWrite,false)
                                .execute();

    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 0시 0분 0초에 실행
    public void resetCount(){
        QJoinedMind joinedMind = QJoinedMind.joinedMind;
        queryFactory.update(joinedMind)
                        .set(joinedMind.count,0)
                .where(joinedMind.count.eq(3))
                                .execute();

    }

}

