package bgty.vt_41.bi.service;

import bgty.vt_41.bi.entity.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(Integer id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    long count();
    User save(User user);
    void delete(User user);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}
