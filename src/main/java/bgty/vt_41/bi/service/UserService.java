package bgty.vt_41.bi.service;

import bgty.vt_41.bi.entity.domain.Playlist;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.util.exceptions.UserException;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(Integer id);

    Optional<User> findByToken(String token);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    long count();

    User save(User user);

    void delete(User user);

    void deleteById(Integer id);

    boolean existsById(Integer id);

    User create(String username, String password, String email) throws UserException;

    User update(User user, String password, String newName, String newPassword, String newEmail) throws UserException;

    Collection<Video> getLoadedVideo(String username);

    Collection<Video> getFavoriteVideo(String username);

    Collection<Playlist> getCreatedPlaylists(String username);
}
