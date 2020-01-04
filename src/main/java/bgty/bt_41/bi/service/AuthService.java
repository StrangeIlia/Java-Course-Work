package bgty.bt_41.bi.service;

import bgty.bt_41.bi.entity.domain.User;

import java.util.Optional;

public interface AuthService {
    String login(String username, String password);
    Optional<User> findByToken(String token);
}
