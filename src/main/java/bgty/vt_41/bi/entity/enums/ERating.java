package bgty.vt_41.bi.entity.enums;

import lombok.Getter;

@Getter
public enum ERating {
    LIKE((byte) 1),
    DISLIKE((byte) 0);

    private byte value;

    ERating(byte value) {
        this.value = value;
    }

    public static ERating valueOf(Integer value) {
        if (value == 1) return LIKE;
        else if (value == 0) return DISLIKE;
        else return null;
    }
}
