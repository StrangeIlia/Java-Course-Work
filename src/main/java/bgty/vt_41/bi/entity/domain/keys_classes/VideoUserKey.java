package bgty.vt_41.bi.entity.domain.keys_classes;

import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class VideoUserKey implements Serializable {
    private User user;
    private Video video;
}
