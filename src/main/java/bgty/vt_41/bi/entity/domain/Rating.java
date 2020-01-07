package bgty.vt_41.bi.entity.domain;

import bgty.vt_41.bi.entity.domain.keys_classes.VideoUserKey;
import bgty.vt_41.bi.entity.enums.ERating;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Ratings")
@IdClass(VideoUserKey.class)
public class Rating {
    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "videoId", nullable = false)
    private Video video;
    @Column(name = "rating", nullable = false)
    private ERating rating;

    @JsonIgnore
    public boolean equalsId(Object object)
    {
        if(this == object) return true;
        if(object instanceof Rating)
        {
            Rating tmp = (Rating) object;
            return tmp.getUser().equals(this.getUser()) && tmp.getVideo().equals(this.getVideo());
        }
        return false;
    }
}
