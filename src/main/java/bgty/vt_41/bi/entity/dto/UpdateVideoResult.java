package bgty.vt_41.bi.entity.dto;

import bgty.vt_41.bi.entity.domain.Video;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateVideoResult extends ORSuccess {
    private Video video;
}
