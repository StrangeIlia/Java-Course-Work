package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.Playlist;
import bgty.vt_41.bi.entity.domain.Rating;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.entity.dto.AuthUserResult;
import bgty.vt_41.bi.entity.dto.ORReject;
import bgty.vt_41.bi.entity.dto.ORSuccess;
import bgty.vt_41.bi.entity.dto.OperationResult;
import bgty.vt_41.bi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<OperationResult> create(@RequestParam String username, @RequestParam String email, @RequestParam String password)
    {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return new ResponseEntity<>(new ORReject("Пользователь с таким логином уже существует"), HttpStatus.OK);
        }
        user = userRepository.findByEmail(username);
        if(user.isPresent()){
            return new ResponseEntity<>(new ORReject("Данная электронная почта уже привязана к другому аккаунту"), HttpStatus.OK);
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        User savedUser = userRepository.save(newUser);
        if(savedUser == null)
            return new ResponseEntity<>(new ORReject(), HttpStatus.OK);
        else
            return new ResponseEntity<>(new AuthUserResult(savedUser.getAccessToken()), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<OperationResult> update(@RequestParam String oldPassword,
                                  @RequestParam(required = false) String username,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String newPassword,
                                  Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
        if(user.checkPassword(oldPassword))
            return new ResponseEntity<>(new ORReject("Неверный пароль"), HttpStatus.OK);

        boolean isUpdate = false;
        if(username != null)
        {
            if(!user.getUsername().equals(username))
            {
                user.setUsername(username);
                isUpdate = true;
            }
        }
        if(email != null)
        {
            if(!user.getEmail().equals(email))
            {
                user.setEmail(email);
                isUpdate = true;
            }
        }
        if(newPassword != null)
        {
            if(!user.getPassword().equals(newPassword))
            {
                user.setPassword(newPassword);
                isUpdate = true;
            }
        }
        if(isUpdate)
        {
            Date date = new Date();
            user.setUpdatedAt(new Timestamp(date.getTime()));
            user = userRepository.save(user);
            if(user == null)
                return new ResponseEntity<>(new ORReject("Ошибка при сохранении изменений"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ORSuccess(), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public void deleteUser(Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
        userRepository.delete(user);
    }

    @GetMapping("/loaded_video")
    public Collection<Video> getLoadedVideo(@RequestParam(required = false) String username,
                                            Authentication authentication)
    {
        if(username != null)
        {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if(optionalUser.isEmpty()) return null;
            else return optionalUser.get().getLoadedVideo();
        }
        else if(authentication != null)
        {
            User user = (User) authentication.getPrincipal();
            return user.getLoadedVideo();
        }
        else return null;
    }

    @GetMapping("/favorite_video")
    public Collection<Video> getFavoriteVideoNonAuth(@RequestParam String username,
                                                     Authentication authentication)
    {
        if(username != null)
        {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if(optionalUser.isEmpty()) return null;
            else return optionalUser.get().getFavoriteVideo();
        }
        else if(authentication != null)
            return ((User) authentication.getPrincipal()).getFavoriteVideo();
        else return null;
    }

    @GetMapping("/playlists")
    public Collection<Playlist> getCreatedPlaylists(Authentication authentication)
    {
        return ((User)authentication.getPrincipal()).getCreatedPlaylists();
    }
}
