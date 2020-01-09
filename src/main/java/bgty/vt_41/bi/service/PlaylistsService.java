package bgty.vt_41.bi.service;

import bgty.vt_41.bi.entity.domain.Playlist;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PlaylistsService {
    List<Playlist> findByAuthor(User user);

    Optional<Playlist> findById(Integer id);

    List<Playlist> findByVideo(Video video);

    long count();

    void deleteById(Integer id);

    void delete(Playlist playlist);

    boolean existsById(Integer id);

    Playlist save(Playlist playlist);

    Playlist create(String name, Integer userId);

    void deleteById(Integer id, Integer userId);

    Collection<Playlist> findByAuthor(String username);

    Collection<Video> findVideosById(Integer id);

    void addVideoInPlaylist(Integer videoId, Integer playlistId, Integer userId);

    void deleteVideoInPlaylist(Integer videoId, Integer playlistId, Integer userId);
}
