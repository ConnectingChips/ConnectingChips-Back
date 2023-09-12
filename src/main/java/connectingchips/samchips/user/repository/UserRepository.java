package connectingchips.samchips.user.repository;

import connectingchips.samchips.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByAccountId(String accountId);

    boolean existsByAccountId(String accountId);
}
