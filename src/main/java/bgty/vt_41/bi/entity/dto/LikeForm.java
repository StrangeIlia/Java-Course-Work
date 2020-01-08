package bgty.vt_41.bi.entity.dto;

import bgty.vt_41.bi.entity.enums.ERating;
import lombok.Data;

@Data
public class LikeForm {
    private int value;

    public ERating getRating() {
        return ERating.valueOf(value);
    }
}
