package bgty.vt_41.bi.entity.domain;

import bgty.vt_41.bi.util.json_serializer.PathFilesSerializer;
import bgty.vt_41.bi.util.json_serializer.UserSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

@Data
@Entity
@Table(name = "Videos")
public class Video implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", length = 30, nullable = false)
    private String name;
    @Column(name = "path", length = 100, nullable = false)
    @JsonSerialize(using = PathFilesSerializer.class)
    private String path;
    @Column(name = "description", length = 50)
    private String description;
    @Column(name = "preview", length = 100, nullable = false)
    @JsonSerialize(using = PathFilesSerializer.class)
    private String preview;
    @Column(name = "numberOfViews", columnDefinition = "int default 0")
    private Integer numberOfViews = 0;
    @Column(name = "createdAt", nullable = false)
    private Timestamp createdAt;
    @Column(name = "updatedAt", nullable = false)
    private Timestamp updatedAt;

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
