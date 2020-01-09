package bgty.vt_41.bi.entity.domain;

import bgty.vt_41.bi.entity.domain.keys_classes.VideoUserKey;
import bgty.vt_41.bi.entity.enums.ERating;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "Ratings")
@IdClass(VideoUserKey.class)
public class Rating implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false, referencedColumnName = "id")
    private User user;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "videoId", nullable = false, referencedColumnName = "id")
    private Video video;
    @Column(name = "rating", nullable = false)
    private ERating rating;

    @JsonIgnore
    public boolean equalsId(Object object) {
        if (this == object) return true;
        if (object instanceof Rating) {
            Rating tmp = (Rating) object;
            return tmp.getUser().equals(this.getUser()) && tmp.getVideo().equals(this.getVideo());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Rating";
    }
}
