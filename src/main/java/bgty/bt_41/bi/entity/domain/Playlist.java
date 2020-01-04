package bgty.bt_41.bi.entity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
    private String createdAt;
    @Column(name = "updateAt", nullable = false)
    private String updatedAt;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinTable(name = "VideoPlaylists",
            joinColumns = @JoinColumn(name = "playlistId", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "videoId", nullable = false))
    private Collection<Video> videos;
}
