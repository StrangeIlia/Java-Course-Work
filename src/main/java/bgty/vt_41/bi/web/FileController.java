package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.RANGE;

@Controller
@RequestMapping("/files")
public class FileController {
    @Autowired
    VideoService videoService;

    public static final String filePath = "files";
    public static final Long maxPartSize = 1024L * 1024L;

    @GetMapping("**")
    public ResponseEntity<ResourceRegion> getFile(HttpServletRequest request)
    {
        String filename = request.getRequestURL().toString();
        try {
            int start = filename.indexOf(filePath) + filePath.length() + 1;
            filename = filename.substring(start);
            UrlResource resource = new FileUrlResource(filename);
            if(resource.exists())
            {
                String range = request.getHeader(RANGE);
                if(range == null)
                {
                    long count = resource.contentLength();
                    ResourceRegion region = new ResourceRegion(resource, 0, count);
                    if(filename.contains(VideoController.basePathForVideo))
                    {
                        Optional<Video> optionalVideo = videoService.findByPath(filename);
                        if(optionalVideo.isPresent())
                        {
                            Video video = optionalVideo.get();
                            video.setNumberOfViews(video.getNumberOfViews() + 1);
                        }
                    }

                    return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                            .contentType(MediaTypeFactory
                                    .getMediaType(resource)
                                    .orElse(MediaType.APPLICATION_OCTET_STREAM))
                            .body(region);
                }
                else
                {
                    String[] ranges = range.replaceFirst("bytes=", "").split("-");
                    Long indexStart = -1L, indexEnd = -1L;
                    if(ranges.length == 1)
                    {
                        indexStart = Long.valueOf(ranges[0]);
                        indexEnd = resource.contentLength();
                    }
                    else if(ranges.length == 2)
                    {
                        indexStart = Long.valueOf(ranges[0]);
                        indexEnd = Long.valueOf(ranges[0]);
                    }
                    if(indexStart < 0 || indexStart > resource.contentLength() || indexEnd < 0 || indexEnd > resource.contentLength() || indexStart > indexEnd)
                        return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value()).build();
                    long count = Long.min(indexEnd - indexStart + 1, maxPartSize);
                    ResourceRegion region = new ResourceRegion(resource, indexStart, count);
                    return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                            .contentType(MediaTypeFactory
                                    .getMediaType(resource)
                                    .orElse(MediaType.APPLICATION_OCTET_STREAM))
                            .body(region);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
