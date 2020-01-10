package bgty.vt_41.bi.entity.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode
@Table(name = "Users",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email", "username"}))
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;
    @Column(nullable = false, length = 30)
    @NotBlank
    private String username;
    @Column(nullable = false, length = 30)
    @JsonIgnore
    private String email;
    @Column(nullable = false, length = 30)
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private String password;
    @Column(nullable = false)
    @JsonIgnore
    private Date createdAt;
    @Column(nullable = false)
    @JsonIgnore
    private Date updatedAt;
    @JsonIgnore
    @Column(nullable = false, length = 36)
    private String authKey;
    @JsonIgnore
    @Column(nullable = false, length = 36)
    private String accessToken;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //Если удален пользователь, то удаляем все видео
    @JsonIgnore
    private Collection<Video> loadedVideo;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL) // все оценки
    @JsonIgnore
    private Collection<Rating> ratings;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL) // и все созданные плейлисты
    @JsonIgnore
    private Collection<Playlist> createdPlaylists;

    public User()
    {
        super();
        Date date = new Date();
        createdAt = new Timestamp(date.getTime());
        updatedAt = new Timestamp(date.getTime());
        accessToken = UUID.randomUUID().toString();
        authKey = UUID.randomUUID().toString();
    }

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
        return this.password.equals(password);
    }

    @JsonIgnore
    public void setPassword(String password)
    {
        this.password = password;
    }

    @JsonIgnore
    public boolean equalsId(Integer object) {
        return object.equals(this.getId());
    }

    @Override
    public String toString() {
        return "User: username=" + getUsername() + "; password=" + getPassword()
                + "; email=" + getEmail() + "; accessToken=" + getAccessToken();
    }
}
