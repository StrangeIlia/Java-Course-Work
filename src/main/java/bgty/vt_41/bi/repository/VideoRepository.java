package bgty.vt_41.bi.repository;

import bgty.vt_41.bi.entity.domain.Video;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends CrudRepository<Video, Integer> {
    public Optional<Video> findByPath(String path);
}
