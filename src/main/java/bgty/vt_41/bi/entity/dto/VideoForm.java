package bgty.vt_41.bi.entity.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VideoForm {
    String name;
    String description;
    MultipartFile video;
    MultipartFile preview;
}
