package bgty.vt_41.bi.service.imp;

import bgty.vt_41.bi.entity.domain.Playlist;
import bgty.vt_41.bi.entity.domain.Rating;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.entity.enums.ERating;
import bgty.vt_41.bi.repository.UserRepository;
import bgty.vt_41.bi.service.UserService;
import bgty.vt_41.bi.util.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public User create(String username, String password, String email) throws UserException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            throw new UserException("Пользователь с таким логином уже существует");
        }
        optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            throw new UserException("Данная электронная почта уже привязана к другому аккаунту");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return userRepository.save(user);
    }

    @Override
    public User update(User user, String password, String newName, String newPassword, String newEmail) throws UserException {
        if (!user.checkPassword(password))
            throw new UserException("Неверный пароль");
        if (newName == null && newPassword == null && newEmail == null)
            throw new UserException("Нет данных для изменения");
        if (newName != null) {
            if (userRepository.findByUsername(newName).isPresent())
                throw new UserException("Неверный пароль");
            user.setUsername(newName);
        }
        if (newEmail != null) {
            if (userRepository.findByEmail(newEmail).isPresent())
                throw new UserException("Неверный пароль");
            user.setUsername(newEmail);
        }
        if (newPassword != null) {
            user.setPassword(newPassword);
        }
        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }

    @Override
    public Collection<Video> getLoadedVideo(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) return null;
        else return optionalUser.get().getLoadedVideo();
    }

    @Override
    public Collection<Video> getFavoriteVideo(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) return null;
        return optionalUser.get()
                .getRatings()
                .stream()
                .filter(x -> x.getRating() == ERating.LIKE)
                .map(Rating::getVideo)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Playlist> getCreatedPlaylists(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(User::getCreatedPlaylists).orElse(null);
    }
}
