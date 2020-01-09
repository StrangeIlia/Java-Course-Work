package bgty.vt_41.bi.service.imp;

import bgty.vt_41.bi.entity.domain.Playlist;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.repository.PlaylistRepository;
import bgty.vt_41.bi.repository.UserRepository;
import bgty.vt_41.bi.repository.VideoRepository;
import bgty.vt_41.bi.service.PlaylistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultPlaylistService implements PlaylistsService {
    @Autowired
    PlaylistRepository playlistRepository;
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public List<Playlist> findByAuthor(User user) {
        return playlistRepository.findByAuthor(user);
    }

    @Override
    public Optional<Playlist> findById(Integer id) {
        return playlistRepository.findById(id);
    }

    @Override
    public List<Playlist> findByVideo(Video video) {
        return playlistRepository.findByVideo(video);
    }

    @Override
    public Collection<Playlist> findByAuthor(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(User::getCreatedPlaylists).orElse(null);
    }

    @Override
    public Collection<Video> findVideosById(Integer id) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        return optionalPlaylist.map(Playlist::getVideos).orElse(null);
    }

    @Override
    public void addVideoInPlaylist(Integer videoId, Integer playlistId, Integer userId) {
        Optional<Video> optionalVideo = videoRepository.findById(videoId);
        if (optionalVideo.isPresent()) {
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
            if (optionalPlaylist.isPresent()) {
                Playlist playlist = optionalPlaylist.get();
                if (playlist.getAuthor().equalsId(userId)) {
                    playlist.getVideos().add(optionalVideo.get());
                    playlist.setUpdatedAt(new Date());
                    playlistRepository.save(playlist);
                }
            }
        }
    }

    @Override
    public void deleteVideoInPlaylist(Integer videoId, Integer playlistId, Integer userId) {
        Optional<Video> optionalVideo = videoRepository.findById(videoId);
        if (optionalVideo.isPresent()) {
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
            if (optionalPlaylist.isPresent()) {
                Playlist playlist = optionalPlaylist.get();
                if (playlist.getAuthor().equalsId(userId)) {
                    playlist.getVideos().remove(optionalVideo.get());
                    playlist.setUpdatedAt(new Date());
                    playlistRepository.save(playlist);
                }
            }
        }
    }

    @Override
    public long count() {
        return playlistRepository.count();
    }

    @Override
    public void deleteById(Integer id) {
        playlistRepository.deleteById(id);
    }

    @Override
    public void delete(Playlist playlist) {
        playlistRepository.delete(playlist);
    }

    @Override
    public boolean existsById(Integer id) {
        return playlistRepository.existsById(id);
    }

    @Override
    public Playlist save(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    @Override
    public Playlist create(String name, Integer userId) {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        userRepository.findById(userId).ifPresent(playlist::setAuthor);
        try {
            return playlistRepository.save(playlist);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteById(Integer id, Integer userId) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        if (optionalPlaylist.isPresent() && optionalPlaylist.get().getAuthor().equalsId(userId))
            playlistRepository.delete(optionalPlaylist.get());
    }
}
