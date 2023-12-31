package connectingchips.samchips.user.repository;

import connectingchips.samchips.user.dto.UserResponseDto;

import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<UserResponseDto.Info> findByUserId(Long userId);

    Optional<UserResponseDto.Info> findInfoByAccountId(String accountId);

    boolean existsByAccountId(String accountId);
}
