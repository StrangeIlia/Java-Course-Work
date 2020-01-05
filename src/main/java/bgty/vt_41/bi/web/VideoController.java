package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.entity.dto.ORReject;
import bgty.vt_41.bi.entity.dto.ORSuccess;
import bgty.vt_41.bi.entity.dto.OperationResult;
import bgty.vt_41.bi.entity.dto.UpdateVideoResult;
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

    @PostMapping("/create")
    public ResponseEntity<OperationResult> createVideo(@RequestParam String name,
                                                      @RequestParam MultipartFile video,
                                                      @RequestParam MultipartFile preview,
                                                      Authentication authentication)
    {
        Date date = new Date();
        Video savedVideo = new Video();
        User user = (User) authentication.getCredentials();
        savedVideo.setAuthor(user);
        savedVideo.setName(name);
        savedVideo.setPath(saveVideo(video));
        savedVideo.setPreview(savePreview(preview));
        savedVideo.setCreatedAt(new java.sql.Timestamp(date.getTime()));
        savedVideo.setUpdatedAt(new java.sql.Timestamp(date.getTime()));
        if(videoRepository.save(savedVideo) != null)
            return new ResponseEntity<>(new ORSuccess(), HttpStatus.OK);
        else
            return new ResponseEntity<>(new ORReject("Не удалось сохранить видео"), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<OperationResult> updateVideo(@RequestParam("id") Integer id,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String description,
                                       @RequestParam(required = false) MultipartFile video,
                                       @RequestParam(required = false) MultipartFile preview,
                                       Authentication authentication)
    {
        Optional<Video> updatedVideo = videoRepository.findById(id);
        if(updatedVideo.isEmpty())
            return new ResponseEntity<>(new ORReject("Нет видео с таким id"), HttpStatus.OK);
        Video savedVideo = updatedVideo.get();
        if(!savedVideo.getAuthor().equals(authentication.getPrincipal()))
            return new ResponseEntity<>(new ORReject("У вас нет прав на обновление этого видео"), HttpStatus.OK);
        boolean isUpdated = false;
        if(name != null) {
            if(!savedVideo.getName().equals(name)){
                savedVideo.setName(name);
                isUpdated = true;
            }
        }
        if(!savedVideo.getDescription().equals(description))
        {
            savedVideo.setName(name);
            isUpdated = true;
        }
        if(video != null)
        {
            File savedFile = new File(basePathForVideo + "/" + savedVideo.getPath());
            try {
                video.transferTo(savedFile);
            } catch (IOException e) {
                return new ResponseEntity<>(new ORReject("Ошибка при загрузке видео"), HttpStatus.OK);
            }
        }
        if(preview != null)
        {
            File savedFile = new File(basePathForPreview + "/" + savedVideo.getPath());
            try {
                preview.transferTo(savedFile);
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
        return new ResponseEntity<>(new UpdateVideoResult(savedVideo), HttpStatus.OK);
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
