package connectingchips.samchips.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import connectingchips.samchips.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static connectingchips.samchips.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UserResponseDto.Info> findByUserId(Long userId) {

        UserResponseDto.Info result = queryFactory
                .select(Projections.constructor(UserResponseDto.Info.class,
                        Expressions.asNumber(userId).as("userId"),
                        user.nickname,
                        user.profileImage,
                        user.roles
                ))
                .from(user)
                .where(user.id.eq(userId))
                .fetchOne();

        return Optional.of(result);
    }
}
