package bgty.vt_41.bi.entity.domain.keys_classes;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class VideoUserKey implements Serializable {
    private Integer user;
    private Integer video;
}
