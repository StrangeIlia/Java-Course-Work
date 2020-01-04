package bgty.bt_41.bi.repository;

import bgty.bt_41.bi.entity.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query(value = "SELECT * FROM Users u WHERE u.username = ?1 and u.password = ?2", nativeQuery = true)
    Optional<User> login(String username, String password);
    //@Query(value = "SELECT u FROM Users u WHERE u.username = ?1")
    Optional<User> findByUsername(String username);
    //@Query(value = "SELECT u FROM Users u WHERE u.email = ?1")
    Optional<User> findByEmail(String email);
    //@Query(value = "SELECT u FROM Users u WHERE u.accessToken = ?1")
    Optional<User> findByAccessToken(String token);
}
