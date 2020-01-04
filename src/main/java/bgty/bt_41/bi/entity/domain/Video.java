package bgty.bt_41.bi.entity.domain;

import bgty.bt_41.bi.entity.json_serializer.UserSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table(name = "Videos")
public class Video implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "path", nullable = false)
    private String path;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "preview", nullable = false)
    private String preview;
    @Column(name = "numberOfViews", nullable = false)
    private Integer numberOfViews;
    @Column(name = "createdAt", nullable = false)
    private String createdAt;
    @Column(name = "updatedAt", nullable = false)
    private String updatedAt;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "author", nullable = false)
    @JsonSerialize(using = UserSerializer.class) // чтобы было "author" : "<name>" вместо "author" : { "username" : "<name>" }
    private User author;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Rating> ratings;

    @ManyToMany(mappedBy = "videos")
    @JsonIgnore
    private Collection<Playlist> playlists;
}
