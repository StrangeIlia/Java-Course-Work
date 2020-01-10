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
    public Collection<Playlist> findByAuthor(User user) {
        return playlistRepository.findByAuthor(user);
    }

    @Override
    public Optional<Playlist> findById(Integer id) {
        return playlistRepository.findById(id);
    }

    @Override
    public Collection<Playlist> findByVideo(Video video) {
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
    public void addVideoInPlaylist(User user, Integer videoId, Integer playlistId) {
        Optional<Video> optionalVideo = videoRepository.findById(videoId);
        if (optionalVideo.isPresent()) {
            Video video = optionalVideo.get();
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
            if (optionalPlaylist.isPresent()) {
                Playlist playlist = optionalPlaylist.get();
                if (playlist.getAuthor().equalsId(user.getId())) {
                    try {
                        playlist.setUpdatedAt(new Date());
                        playlist.getVideos().add(video);
                        playlistRepository.save(playlist);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void deleteVideoInPlaylist(User user, Integer videoId, Integer playlistId) {
        Optional<Video> optionalVideo = videoRepository.findById(videoId);
        if (optionalVideo.isPresent()) {
            Video video = optionalVideo.get();
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
            if (optionalPlaylist.isPresent()) {
                Playlist playlist = optionalPlaylist.get();
                if (playlist.getAuthor().equalsId(user.getId())) {
                    playlist.getVideos().size(); //Делаем подгрузку (защита от LAZY)
                    video.getPlaylists().size(); //Делаем подгрузку (защита от LAZY)
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
    public Playlist create(User user, String name) {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setAuthor(user);
        try {
            return playlistRepository.save(playlist);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteById(User user, Integer id) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        if (optionalPlaylist.isPresent() && optionalPlaylist.get().getAuthor().equalsId(user.getId()))
            playlistRepository.delete(optionalPlaylist.get());
    }
}
