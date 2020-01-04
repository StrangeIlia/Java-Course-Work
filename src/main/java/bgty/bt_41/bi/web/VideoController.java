package bgty.bt_41.bi.web;

import bgty.bt_41.bi.entity.domain.Video;
import bgty.bt_41.bi.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


}
