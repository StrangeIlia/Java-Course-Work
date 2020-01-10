package bgty.vt_41.bi.service;

import bgty.vt_41.bi.entity.domain.Playlist;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;

import java.util.Collection;
import java.util.Optional;

public interface PlaylistsService {
    Collection<Playlist> findByAuthor(User user);

    Optional<Playlist> findById(Integer id);

    Collection<Playlist> findByVideo(Video video);

    long count();

    void deleteById(Integer id);

    void delete(Playlist playlist);

    boolean existsById(Integer id);

    Playlist save(Playlist playlist);

    Playlist create(User user, String name);

    void deleteById(User user, Integer id);

    Collection<Playlist> findByAuthor(String username);

    Collection<Video> findVideosById(Integer id);

    void addVideoInPlaylist(User user, Integer videoId, Integer playlistId);

    void deleteVideoInPlaylist(User user, Integer videoId, Integer playlistId);
}
