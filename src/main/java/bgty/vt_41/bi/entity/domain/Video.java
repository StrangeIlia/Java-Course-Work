package bgty.vt_41.bi.entity.domain;

import bgty.vt_41.bi.util.FileHelper;
import bgty.vt_41.bi.util.json_serializer.DateSerializer;
import bgty.vt_41.bi.util.json_serializer.PathFilesSerializer;
import bgty.vt_41.bi.util.json_serializer.UserSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

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
    @JsonSerialize(using = DateSerializer.class)
    private Date createdAt;
    @Column(name = "updatedAt", nullable = false)
    @JsonSerialize(using = DateSerializer.class)
    private Date updatedAt;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "author", nullable = false)
    @JsonSerialize(using = UserSerializer.class)
    // чтобы было "author" : "<name>" вместо "author" : { "username" : "<name>" }
    private User author;

    public Video() {
        super();
        Date date = new Date();
        createdAt = new Timestamp(date.getTime());
        updatedAt = new Timestamp(date.getTime());
    }

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //Если видео удалено, то удаляем все оценки к нему
    @JsonIgnore
    private Collection<Rating> ratings;

    @ManyToMany(mappedBy = "videos")
    @JsonIgnore
    private Collection<Playlist> playlists;

    @JsonIgnore
    public boolean equalsId(Object object)
    {
        if(this == object) return true;
        if(object instanceof Video)
        {
            Video tmp = (Video) object;
            return tmp.getId().equals(this.getId());
        }
        return false;
    }

    @PreRemove //Удаляем только связи и файлы!!!
    private void preRemove() {
        FileHelper.deleteFile(this.getPath());
        FileHelper.deleteFile(this.getPreview());
        getPlaylists().clear();
    }

    @Override
    public String toString() {
        return "Video: name=" + getName() + "; path=" + getPath() + "; preview=" + getPreview();
    }
}
