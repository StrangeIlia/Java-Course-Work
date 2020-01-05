package bgty.vt_41.bi.entity.domain;


import bgty.vt_41.bi.entity.enums.ERating;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
@Table(name = "Users",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @JsonIgnore
    private Integer id;
    @Column(nullable = false)
    @NotBlank
    private String username;
    @Column(nullable = false)
    @JsonIgnore
    private String email;
    @Column(nullable = false)
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private String password;
    @Column(nullable = false)
    @JsonIgnore
    private Timestamp createdAt;
    @Column(nullable = false)
    @JsonIgnore
    private Timestamp updatedAt;
    @Column(nullable = false)
    @JsonIgnore
    private String authKey;
    @Column(nullable = false)
    @JsonIgnore
    private String accessToken;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Video> loadedVideo;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Rating> ratings;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Playlist> createdPlaylists;

    @JsonIgnore
    public Collection<Video> getFavoriteVideo()
    {
        Collection<Video> videos = new ArrayList<>();
        ratings.forEach(x -> {
            if(x.getRating() == ERating.LIKE)
                videos.add(x.getVideo());
        });
        return videos;
    }

    public User(){ super(); }

    public User(String username, String email, String password)
    {
        super();
        this.setUsername(username);
        this.setEmail(email);
        this.setPassword(password);
    }


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    public boolean checkPassword(String password)
    {
        return  this.password == password;
    }

    @JsonIgnore
    public void setPassword(String password)
    {
        this.password = password;
    }
}
