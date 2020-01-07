package bgty.vt_41.bi.service.imp;

import bgty.vt_41.bi.entity.domain.Playlist;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.repository.PlaylistRepository;
import bgty.vt_41.bi.service.PlaylistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultPlaylistService implements PlaylistsService {
    @Autowired
    PlaylistRepository playlistRepository;


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
}
