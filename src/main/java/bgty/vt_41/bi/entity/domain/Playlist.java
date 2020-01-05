package bgty.vt_41.bi.entity.domain;

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
@Table(name = "Playlists")
public class Playlist implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "createdAt",nullable = false)
    private Timestamp createdAt;
    @Column(name = "updateAt", nullable = false)
    private Timestamp updatedAt;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "author", nullable = false)
    @JsonSerialize(using = UserSerializer.class)
    private User author;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinTable(name = "VideoPlaylists",
            joinColumns = @JoinColumn(name = "playlistId", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "videoId", nullable = false))
    private Collection<Video> videos;
}
