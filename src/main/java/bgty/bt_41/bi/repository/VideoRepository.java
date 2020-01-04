package bgty.bt_41.bi.repository;

import bgty.bt_41.bi.entity.domain.Video;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends CrudRepository<Video, Integer> {

}
