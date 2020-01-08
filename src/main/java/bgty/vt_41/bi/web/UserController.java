package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.Playlist;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.entity.dto.*;
import bgty.vt_41.bi.service.UserService;
import bgty.vt_41.bi.service.VideoService;
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
    UserService userService;
    @Autowired
    VideoService videoService;

    @PostMapping(value = "/create")
    public ResponseEntity<OperationResult> create(@RequestBody CreationUserForm creationUserForm) {
        if (!creationUserForm.isValid())
            return ResponseEntity.ok(new ORReject("Не верная форма данных"));
        Optional<User> user = userService.findByUsername(creationUserForm.getUsername());
        if (user.isPresent()) {
            return ResponseEntity.ok(new ORReject("Пользователь с таким логином уже существует"));
        }
        user = userService.findByEmail(creationUserForm.getEmail());
        if (user.isPresent()) {
            return ResponseEntity.ok(new ORReject("Данная электронная почта уже привязана к другому аккаунту"));
        }
        User newUser = new User();
        newUser.setUsername(creationUserForm.getUsername());
        newUser.setEmail(creationUserForm.getEmail());
        newUser.setPassword(creationUserForm.getPassword());
        User savedUser = userService.save(newUser);
        if(savedUser == null)
            return ResponseEntity.ok(new ORReject());
        else
            return ResponseEntity.ok(new AuthUserResult(savedUser.getAccessToken()));
    }

    @PatchMapping("/update")
    public ResponseEntity<OperationResult> update(@RequestBody UpdateUserForm updateUserForm,
                                                  Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
        if(user.checkPassword(updateUserForm.getOldPassword()))
            return new ResponseEntity<>(new ORReject("Неверный пароль"), HttpStatus.OK);

        boolean isUpdate = false;
        if(updateUserForm.getUsername() != null)
        {
            if(!user.getUsername().equals(updateUserForm.getUsername()))
            {
                user.setUsername(updateUserForm.getUsername());
                isUpdate = true;
            }
        }
        if(updateUserForm.getEmail() != null)
        {
            if(!user.getEmail().equals(updateUserForm.getEmail()))
            {
                user.setEmail(updateUserForm.getEmail());
                isUpdate = true;
            }
        }
        if(updateUserForm.getNewPassword() != null)
        {
            if(!user.getPassword().equals(updateUserForm.getNewPassword()))
            {
                user.setPassword(updateUserForm.getNewPassword());
                isUpdate = true;
            }
        }
        if(isUpdate)
        {
            Date date = new Date();
            user.setUpdatedAt(new Timestamp(date.getTime()));
            user = userService.save(user);
            if(user == null)
                return new ResponseEntity<>(new ORReject("Ошибка при сохранении изменений"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ORSuccess(), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public void deleteUser(Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
        userService.delete(user);
    }

    @GetMapping("/loaded_video")
    public Collection<Video> getLoadedVideo(@RequestParam String username) {
        if (!username.equals("")) {
            Optional<User> optionalUser = userService.findByUsername(username);
            if (optionalUser.isEmpty()) return null;
            else return optionalUser.get().getLoadedVideo();
        }
        return null;
    }

    @GetMapping("/favorite_video")
    public Collection<Video> getFavoriteVideo(@RequestParam String username) {
        if (username != null) {
            Optional<User> optionalUser = userService.findByUsername(username);
            if (optionalUser.isEmpty()) return null;
            Collection<Video> videos = videoService.getFavorites(optionalUser.get());
            return videos;
        }
        return null;
    }

    @GetMapping("/playlists")
    public Collection<Playlist> getCreatedPlaylists(@RequestParam(required = false) String username) {
        if (!username.equals("")) {
            Optional<User> optionalUser = userService.findByUsername(username);
            if (optionalUser.isEmpty()) return null;
            else return optionalUser.get().getCreatedPlaylists();
        }
        return null;
    }
}
