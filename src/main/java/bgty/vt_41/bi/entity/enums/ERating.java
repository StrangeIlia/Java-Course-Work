package bgty.vt_41.bi.entity.enums;

import lombok.Getter;

@Getter
public enum ERating {
    LIKE((byte)1),
    DISLIKE((byte)0);

    private byte value;

    ERating(byte value){
        this.value = value;
    }
}
