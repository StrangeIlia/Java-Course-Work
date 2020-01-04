package bgty.vt_41.bi.entity.domain;

import bgty.vt_41.bi.entity.json_serializer.UserSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
    private String path;
    @Column(name = "description", length = 50)
    private String description;
    @Column(name = "preview", length = 100, nullable = false)
    private String preview;
    @Column(name = "numberOfViews", columnDefinition = "int default 0")
    private Integer numberOfViews;
    @Column(name = "createdAt", nullable = false)
    private Date createdAt;
    @Column(name = "updatedAt", nullable = false)
    private Date updatedAt;

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
