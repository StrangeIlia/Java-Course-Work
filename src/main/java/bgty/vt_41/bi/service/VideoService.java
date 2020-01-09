package bgty.vt_41.bi.service;

import bgty.vt_41.bi.entity.domain.Rating;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.entity.enums.ERating;

import java.util.Optional;

public interface VideoService {
    Optional<Video> findById(Integer id);

    Optional<Video> findByPath(String path);

    void liked(User user, Video video);

    void disliked(User user, Video video);

    void removeGrade(User user, Video video);

    long count();

    Video save(Video video);

    void delete(Video video);

    Iterable<Video> findAll();

    void deleteById(Integer id);

    boolean existsById(Integer id);

    void rating(User user, Video video, ERating rating);

    Optional<Rating> isLiked(User user, Video video);

    long countLiked(Video video);

    long countDisliked(Video video);
}
