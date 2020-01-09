package bgty.vt_41.bi.entity.domain;

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
@Table(name = "Playlists")
public class Playlist implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "createdAt", nullable = false)
    private Date createdAt;
    @Column(name = "updatedAt", nullable = false)
    private Date updatedAt;

    public Playlist()
    {
        Date date = new Date();
        createdAt = new Timestamp(date.getTime());
        updatedAt = new Timestamp(date.getTime());
    }

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "author", nullable = false)
    @JsonSerialize(using = UserSerializer.class)
    private User author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(name = "VideoPlaylists",
            joinColumns = @JoinColumn(name = "playlistId", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "videoId", nullable = false))
    private Collection<Video> videos;

    @JsonIgnore
    public boolean equalsId(Object object)
    {
        if(this == object) return true;
        if(object instanceof Playlist)
        {
            Playlist tmp = (Playlist) object;
            return tmp.getId().equals(this.getId());
        }
        return false;
    }

    @PreRemove //Удаляем только связи!!!
    private void preRemove() {
        getVideos().clear();
    }

    @Override
    public String toString() {
        return "Playlist: name=" + getName();
    }
}
