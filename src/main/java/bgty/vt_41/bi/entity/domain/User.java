package bgty.vt_41.bi.entity.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Date;
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
    private String password;
    @Column(nullable = false)
    @JsonIgnore
    private Date createdAt;
    @Column(nullable = false)
    @JsonIgnore
    private Date updatedAt;
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
    private Collection<Rating> favoriteVideos;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Playlist> createdPlaylists;

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
}
