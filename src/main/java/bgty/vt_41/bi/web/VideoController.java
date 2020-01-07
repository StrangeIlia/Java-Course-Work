package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.entity.dto.*;
import bgty.vt_41.bi.service.UserService;
import bgty.vt_41.bi.service.VideoService;
import bgty.vt_41.bi.util.FileHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("api/videos")
public class VideoController {
    @Autowired
    VideoService videoRepository;

    @Autowired
    UserService userService;

    public static final String basePathForVideo = "uploads/videos";
    public static final String basePathForPreview = "uploads/previews";

    @GetMapping(value = {"", "/index"})
    public Iterable<Video> getAllVideo()
    {
        return videoRepository.findAll();
    }

    @GetMapping("/view")
    public Optional<Video> getVideo(@RequestParam Integer id)
    {
        return videoRepository.findById(id);
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<OperationResult> createVideo(@ModelAttribute VideoForm sendVideo,
                                                       Authentication authentication)
    {

        Video savedVideo = new Video();
        User user = (User) authentication.getPrincipal();
        //savedVideo.setAuthor(user); //НИКОГДА НЕ ДЕЛАЕТЕ ТАК
        userService.findById(user.getId()).ifPresent(savedVideo::setAuthor); //Правильно так
        /*Optional<User> optionalUser = Optional.ofNullable(user);
        optionalUser.ifPresent(savedVideo::setAuthor);*/ // Попытка схитрить не удалась ХД (нужно подгружать связи)
        savedVideo.setName(sendVideo.getName());
        savedVideo.setDescription(sendVideo.getDescription());
        savedVideo.setPath(saveVideo(sendVideo.getVideo()));
        savedVideo.setPreview(savePreview(sendVideo.getPreview()));
        try {
            if(videoRepository.save(savedVideo) != null)
                return ResponseEntity.ok(new ORSuccess());
            else
                return ResponseEntity.ok(new ORReject("Не удалось сохранить видео"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(videoRepository.save(savedVideo) != null)
            return new ResponseEntity<>(new ORSuccess(), HttpStatus.OK);
        else
            return new ResponseEntity<>(new ORReject("Не удалось сохранить видео"), HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<OperationResult> updateVideo(@RequestParam Integer id,
                                                       @ModelAttribute VideoForm sendVideo,
                                                       Authentication authentication)
    {
        Optional<Video> updatedVideo = videoRepository.findById(id);
        if(updatedVideo.isEmpty())
            return ResponseEntity.ok(new ORReject("Нет видео с таким id"));
        Video savedVideo = updatedVideo.get();
        if(!savedVideo.getAuthor().equalsId(authentication.getPrincipal()))
            return ResponseEntity.ok(new ORReject("У вас нет прав на обновление этого видео"));
        boolean isUpdated = false;
        if(sendVideo.getName() != null) {
            if(!savedVideo.getName().equals(sendVideo.getName())){
                savedVideo.setName(sendVideo.getName());
                isUpdated = true;
            }
        }
        if(savedVideo.getDescription() != null || sendVideo.getDescription() != null)
        {
            if(savedVideo.getDescription() == null || !savedVideo.getDescription().equals(sendVideo.getDescription()))
            {
                savedVideo.setDescription(sendVideo.getDescription());
                isUpdated = true;
            }
        }

        if(sendVideo.getVideo() != null)
        {
            try {
                FileHelper.rebase(sendVideo.getVideo(), savedVideo.getPath());
                savedVideo.setNumberOfViews(0);
            } catch (IOException e) {
                return ResponseEntity.ok(new ORReject("Ошибка при загрузке видео"));
            }
        }
        if(sendVideo.getPreview() != null)
        {
            try {
                FileHelper.rebase(sendVideo.getPreview(), savedVideo.getPreview());
            } catch (IOException e) {
                return ResponseEntity.ok(new ORReject("Ошибка при загрузке превью"));
            }
        }
        if(isUpdated)
        {
            Date date = new Date();
            savedVideo.setUpdatedAt(new Timestamp(date.getTime()));
            savedVideo = videoRepository.save(savedVideo);
            if(savedVideo == null)
                return ResponseEntity.ok(new ORReject("Ошибка при сохранении видео"));
        }
        return ResponseEntity.ok(new UpdateVideoResult(savedVideo));
    }

    @DeleteMapping("/delete")
    public void deleteVideo(@RequestParam("id") Integer id,
                            Authentication authentication)
    {
        Optional<Video> optionalVideo = videoRepository.findById(id);
        if(optionalVideo.isPresent())
        {
            Video video = optionalVideo.get();
            User author = video.getAuthor();
            if(author.equalsId(authentication.getPrincipal()))
                videoRepository.delete(video);
        }
    }

    private String saveVideo(MultipartFile video) {
        return FileHelper.saveFile(video, basePathForVideo);
    }

    private String savePreview(MultipartFile preview) {
        return FileHelper.saveFile(preview, basePathForPreview);
    }
}
