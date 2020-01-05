package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.entity.dto.*;
import bgty.vt_41.bi.repository.VideoRepository;
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
    VideoRepository videoRepository;

    private String basePathForVideo = "uploads/videos";
    private String basePathForPreview = "uploads/previews";

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
        Date date = new Date();
        Video savedVideo = new Video();
        User user = (User) authentication.getCredentials();
        savedVideo.setAuthor(user);
        savedVideo.setName(sendVideo.getName());
        savedVideo.setDescription(sendVideo.getDescription());
        savedVideo.setPath(saveVideo(sendVideo.getVideo()));
        savedVideo.setPreview(savePreview(sendVideo.getPreview()));
        savedVideo.setCreatedAt(new java.sql.Timestamp(date.getTime()));
        savedVideo.setUpdatedAt(new java.sql.Timestamp(date.getTime()));
        try {
            if(videoRepository.save(savedVideo) != null)
                return new ResponseEntity<>(new ORSuccess(), HttpStatus.OK);
            else
                return new ResponseEntity<>(new ORReject("Не удалось сохранить видео"), HttpStatus.OK);
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
            return new ResponseEntity<>(new ORReject("Нет видео с таким id"), HttpStatus.OK);
        Video savedVideo = updatedVideo.get();
        if(!savedVideo.getAuthor().equals(authentication.getPrincipal()))
            return new ResponseEntity<>(new ORReject("У вас нет прав на обновление этого видео"), HttpStatus.OK);
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
                savedVideo.setName(sendVideo.getDescription());
                isUpdated = true;
            }
        }

        if(sendVideo.getVideo() != null)
        {
            File savedFile = new File(basePathForVideo + "/" + savedVideo.getPath());
            try {
                sendVideo.getVideo().transferTo(savedFile);
            } catch (IOException e) {
                return new ResponseEntity<>(new ORReject("Ошибка при загрузке видео"), HttpStatus.OK);
            }
        }
        if(sendVideo.getPreview() != null)
        {
            File savedFile = new File(basePathForPreview + "/" + savedVideo.getPath());
            try {
                sendVideo.getPreview().transferTo(savedFile);
            } catch (IOException e) {
                return new ResponseEntity<>(new ORReject("Ошибка при загрузке превью"), HttpStatus.OK);
            }
        }
        if(isUpdated)
        {
            Date date = new Date();
            savedVideo.setUpdatedAt(new Timestamp(date.getTime()));
            savedVideo = videoRepository.save(savedVideo);
            if(savedVideo == null)
                return new ResponseEntity<>(new ORReject("Ошибка при сохранении видео"), HttpStatus.OK);
        }
        return ResponseEntity.ok(new UpdateVideoResult(savedVideo));
    }

    @DeleteMapping("/delete")
    public void deleteVideo(@RequestParam("id") Integer id,
                            Authentication authentication)
    {
        Optional<Video> video = videoRepository.findById(id);
        if(video.isPresent() && video.get().getAuthor().equals(authentication.getPrincipal()))
            videoRepository.delete(video.get());
    }

    private String saveVideo(MultipartFile video) {
        return FileHelper.saveFile(video, basePathForVideo);
    }

    private String savePreview(MultipartFile preview) {
        return FileHelper.saveFile(preview, basePathForPreview);
    }
}
