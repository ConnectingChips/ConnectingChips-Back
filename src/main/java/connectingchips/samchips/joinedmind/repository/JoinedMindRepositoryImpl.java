package connectingchips.samchips.joinedmind.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import connectingchips.samchips.joinedmind.dto.service.JoinCheckOutPut;
import connectingchips.samchips.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static connectingchips.samchips.joinedmind.entity.QJoinedMind.joinedMind;
import static connectingchips.samchips.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class JoinedMindRepositoryImpl implements JoinedMindRepositoryCustom{

    public static final int ALREADY_JOINING = 1;
    private final JPAQueryFactory queryFactory;

}
