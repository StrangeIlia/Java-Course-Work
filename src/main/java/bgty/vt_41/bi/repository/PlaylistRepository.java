package bgty.vt_41.bi.repository;

import bgty.vt_41.bi.entity.domain.Playlist;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, Integer> {
    Collection<Playlist> findByAuthor(User user);

    @Query(value = "SELECT p.* FROM VideoPlaylists vp inner join Playlist p WHERE vp.video = :video", nativeQuery = true)
    Collection<Playlist> findByVideo(@Param("video") Video video);
}
