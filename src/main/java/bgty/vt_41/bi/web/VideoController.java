package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.entity.dto.ORReject;
import bgty.vt_41.bi.entity.dto.ORSuccess;
import bgty.vt_41.bi.entity.dto.OperationResult;
import bgty.vt_41.bi.repository.VideoRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("api/videos")
public class VideoController {
    @Autowired
    VideoRepository controller;

    @GetMapping("/index")
    public Iterable<Video> getAllVideo()
    {
        return controller.findAll();
    }

    @GetMapping("/view")
    public Optional<Video> getVideo(@RequestParam Integer id)
    {
        return controller.findById(id);
    }

    @PostMapping("/create")
    public OperationResult createVideo(@RequestParam("name") String name,
                                       @RequestParam("video") MultipartFile video,
                                       @RequestParam("preview") MultipartFile preview,
                                       Authentication authentication)
    {
        Date date = new Date();
        Video savedVideo = new Video();
        User user = (User) authentication.getCredentials();
        savedVideo.setAuthor(user);
        savedVideo.setName(name);
        savedVideo.setPath(saveVideo(video));
        savedVideo.setPreview(savePreview(preview));
        savedVideo.setCreatedAt(new java.sql.Date(date.getTime()));
        savedVideo.setUpdatedAt(new java.sql.Date(date.getTime()));
        if(controller.save(savedVideo) != null)
            return new ORSuccess();
        else
            return new ORReject("Не удалось сохранить видео");
    }

    private String saveVideo(MultipartFile video) {
        return saveFile(video, "uploads/videos");
    }

    private String savePreview(MultipartFile preview) {
        return saveFile(preview, "uploads/previews");
    }

    private String saveFile(MultipartFile file, String basePath)
    {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String path = basePath + "/" + dateFormat.format(date);
        String newFileName = Integer.toString((date.toString() + file.getName()).hashCode()) + "." + extension;
        File savedFile = new File(path + "/" + newFileName );
        try {
            file.transferTo(savedFile);
        } catch (IOException e) {
            return "";
        }
        return newFileName;
    }
}
