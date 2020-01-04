package bgty.vt_41.bi.entity.domain;

import bgty.vt_41.bi.entity.domain.keys_classes.VideoUserKey;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Ratings")
@IdClass(VideoUserKey.class)
public class Rating {
    @Id
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    @Id
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "videoId", nullable = false)
    private Video video;
    @Column(name = "rating", nullable = false)
    private Byte rating;
}
