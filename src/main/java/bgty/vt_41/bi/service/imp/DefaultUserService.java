package bgty.vt_41.bi.service.imp;

import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.repository.UserRepository;
import bgty.vt_41.bi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultUserService implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByToken(String token) { return userRepository.findByAccessToken(token); }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }
}
