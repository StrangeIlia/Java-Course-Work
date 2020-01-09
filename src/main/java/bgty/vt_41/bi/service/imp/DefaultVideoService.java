package bgty.vt_41.bi.service.imp;

import bgty.vt_41.bi.entity.domain.Rating;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.entity.enums.ERating;
import bgty.vt_41.bi.repository.RatingRepository;
import bgty.vt_41.bi.repository.UserRepository;
import bgty.vt_41.bi.repository.VideoRepository;
import bgty.vt_41.bi.service.VideoService;
import bgty.vt_41.bi.util.exceptions.VideoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
public class DefaultVideoService implements VideoService {
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<Video> findById(Integer id) {
        return videoRepository.findById(id);
    }

    @Override
    public Optional<Video> findByPath(String path) {
        return videoRepository.findByPath(path);
    }

    @Override
    public void liked(User user, Video video) {
        Optional<Rating> optionalRating = ratingRepository.findByUserAndVideo(user, video);
        Rating rating;
        if(optionalRating.isPresent())
            rating = optionalRating.get();
        else
        {
            rating = new Rating();
            rating.setUser(user);
            rating.setVideo(video);
        }
        rating.setRating(ERating.LIKE);
        ratingRepository.save(rating);
    }

    @Override
    public void disliked(User user, Video video) {
        Optional<Rating> optionalRating = ratingRepository.findByUserAndVideo(user, video);
        Rating rating;
        if(optionalRating.isPresent())
            rating = optionalRating.get();
        else
        {
            rating = new Rating();
            rating.setUser(user);
            rating.setVideo(video);
        }
        rating.setRating(ERating.DISLIKE);
        ratingRepository.save(rating);
    }

    @Override
    public void removeGrade(User user, Video video) {
        Optional<Rating> optionalRating = ratingRepository.findByUserAndVideo(user, video);
        optionalRating.ifPresent(rating -> ratingRepository.delete(rating));
    }

    @Override
    public long count() {
        return videoRepository.count();
    }

    @Override
    public Video save(Video video) {
        return videoRepository.save(video);
    }

    @Override
    public void delete(Video video) {
        videoRepository.delete(video);
    }

    @Override
    public Iterable<Video> findAll() {
        return videoRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        videoRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return videoRepository.existsById(id);
    }

    @Override
    public void rating(User user, Video video, ERating rating) {
        Optional<Rating> optionalRating = ratingRepository.findByUserAndVideo(user, video);
        if (rating == null) {
            optionalRating.ifPresent(value -> ratingRepository.delete(value));
        } else {
            Rating newRating;
            if (optionalRating.isPresent())
                newRating = optionalRating.get();
            else {
                newRating = new Rating();
                userRepository.findById(user.getId()).ifPresent(newRating::setUser);
                videoRepository.findById(video.getId()).ifPresent(newRating::setVideo);
            }

            newRating.setRating(rating);
            ratingRepository.save(newRating);
        }
    }

    @Override
    public Optional<Rating> isLiked(User user, Video video) {
        return video.getRatings().stream().filter(x -> x.getUser().equalsId(user.getId())).findFirst();
    }

    @Override
    public long countLiked(Video video) {
        Collection<Rating> ratings = video.getRatings();
        return ratings.stream().filter(x -> x.getRating().equals(ERating.LIKE)).count();
    }

    @Override
    public long countDisliked(Video video) {
        Collection<Rating> ratings = video.getRatings();
        return ratings.stream().filter(x -> x.getRating().equals(ERating.DISLIKE)).count();
    }

    @Override
    public Video create(User user, String name, String description, String path, String preview) throws VideoException {
        Video video = new Video();
        if (user == null)
            throw new VideoException("Не указан пользователь");
        if (name == null)
            throw new VideoException("Не указано название видео");
        if (path == null)
            throw new VideoException("Не удалось получить файл видео");
        if (preview == null)
            throw new VideoException("Не удалось получить файл превью");
        video.setAuthor(user);
        video.setName(name);
        video.setDescription(description);
        video.setPath(path);
        video.setPreview(preview);
        return videoRepository.save(video);
    }

    @Override
    public Video update(Integer id, User user, String name, String description, String path, String preview) throws VideoException {
        Optional<Video> optionalVideo = videoRepository.findById(id);
        if (optionalVideo.isEmpty())
            throw new VideoException("Нет видео с таким id");
        Video video = optionalVideo.get();
        if (!video.getAuthor().equalsId(user.getId()))
            throw new VideoException("У вас нет прав, на изменение этого видео");

        if (name == null && description == null && path == null && preview == null)
            throw new VideoException("Нет данных для изменения");

        if (name != null)
            video.setName(name);
        if (path != null)
            video.setPath(path);
        if (preview != null)
            video.setPreview(preview);
        video.setDescription(description);
        video.setUpdatedAt(new Date());

        return videoRepository.save(video);
    }

    @Override
    public void deleteById(User user, Integer videoId) {
        Optional<Video> optionalVideo = videoRepository.findById(videoId);
        if (optionalVideo.isPresent() && optionalVideo.get().getAuthor().equalsId(user.getId()))
            videoRepository.delete(optionalVideo.get());
    }
}
