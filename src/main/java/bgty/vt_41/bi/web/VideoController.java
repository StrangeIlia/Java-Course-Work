package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.Rating;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.entity.dto.*;
import bgty.vt_41.bi.entity.enums.ERating;
import bgty.vt_41.bi.service.UserService;
import bgty.vt_41.bi.service.VideoService;
import bgty.vt_41.bi.util.FileHelper;
import bgty.vt_41.bi.util.exceptions.VideoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("api/videos")
public class VideoController {
    @Autowired
    VideoService videoService;

    @Autowired
    UserService userService;

    public static final String basePathForVideo = "uploads/videos";
    public static final String basePathForPreview = "uploads/previews";

    @GetMapping(value = {"", "/index"})
    public Iterable<Video> getAllVideo()
    {
        return videoService.findAll();
    }

    @GetMapping("/view")
    public Optional<Video> getVideo(@RequestParam Integer id)
    {
        return videoService.findById(id);
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<OperationResult> createVideo(@ModelAttribute VideoForm sendVideo,
                                                       Authentication authentication) {
        String path = saveVideo(sendVideo.getVideo());
        String preview = saveVideo(sendVideo.getPreview());
        User user = (User) authentication.getPrincipal();
        try {
            videoService.create(
                    user,
                    sendVideo.getName(),
                    sendVideo.getDescription(),
                    path,
                    preview
            );
            return ResponseEntity.ok(new ORSuccess());
        } catch (VideoException e) {
            return ResponseEntity.ok(new ORReject(e.getMessage()));
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<OperationResult> updateVideo(@RequestParam Integer id,
                                                       @ModelAttribute VideoForm sendVideo,
                                                       Authentication authentication) throws IOException {
        Optional<Video> optionalVideo = videoService.findById(id);
        if (optionalVideo.isPresent()) {
            String path = FileHelper.rebase(sendVideo.getVideo(), optionalVideo.get().getPath());
            String preview = FileHelper.rebase(sendVideo.getPreview(), optionalVideo.get().getPreview());
            User user = (User) authentication.getPrincipal();
            try {
                Video video = videoService.update(
                        id,
                        user,
                        sendVideo.getName(),
                        sendVideo.getDescription(),
                        path,
                        preview
                );
                return ResponseEntity.ok(new UpdateVideoResult(video));
            } catch (VideoException e) {
                return ResponseEntity.ok(new ORReject(e.getMessage()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok(new ORReject("Видео с таким id не существует"));
    }

    @DeleteMapping("/delete")
    public void deleteVideo(@RequestParam("id") Integer id,
                            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        videoService.deleteById(user, id);
    }

    @GetMapping("/rating")
    public Object getRating(@RequestParam Integer id) {
        Optional<Video> optionalVideo = videoService.findById(id);
        if (optionalVideo.isEmpty()) return null;
        else return new Object() {
            public long liked = videoService.countLiked(optionalVideo.get());
            public long disliked = videoService.countDisliked(optionalVideo.get());
        };
    }

    @PostMapping("/rating")
    public ResponseEntity<OperationResult> setRating(@RequestParam Integer id,
                                                     @RequestBody ERating rating,
                                                     Authentication authentication) {
        Optional<Video> optionalVideo = videoService.findById(id);
        if (optionalVideo.isEmpty()) return ResponseEntity.ok(new ORReject("Нет видео с таким id"));
        if (rating == ERating.LIKE || rating == ERating.DISLIKE) {
            User pseudoUser = (User) authentication.getPrincipal();
            Optional<User> optionalUser = userService.findById(pseudoUser.getId());
            User user = optionalUser.get();
            videoService.rating(user, optionalVideo.get(), rating);
        }
        return ResponseEntity.ok(new ORReject("Не верно указана оценка"));
    }

    @GetMapping("/liked")
    public Object isLiked(@RequestParam Integer id, Authentication authentication) {
        Optional<Video> optionalVideo = videoService.findById(id);
        if (optionalVideo.isPresent()) {
            User pseudoUser = (User) authentication.getPrincipal();
            Optional<Rating> optionalRating = videoService.isLiked(pseudoUser, optionalVideo.get());
            if (optionalRating.isPresent())
                return new Object() {
                    public String liked = optionalRating.get().getRating() == ERating.LIKE ? "like" : "dislike";
                };
        }
        return new Object() {
            public String liked = "none";
        };
    }

    @PostMapping("/liked")
    public void setLiked(@RequestParam Integer id, @RequestBody LikeForm like, Authentication authentication) {
        Optional<Video> optionalVideo = videoService.findById(id);
        if (optionalVideo.isPresent()) {
            User pseudoUser = (User) authentication.getPrincipal();
            videoService.rating(pseudoUser, optionalVideo.get(), like.getRating());
        }
    }

    private String saveVideo(MultipartFile video) {
        return FileHelper.saveFile(video, basePathForVideo);
    }

    private String savePreview(MultipartFile preview) {
        return FileHelper.saveFile(preview, basePathForPreview);
    }
}
