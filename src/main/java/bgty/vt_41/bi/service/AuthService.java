package bgty.vt_41.bi.service;

import bgty.vt_41.bi.entity.domain.User;

import java.util.Optional;

public interface AuthService {
    String login(String username, String password);
    boolean logout(User user);
    Optional<User> findByToken(String token);
}
