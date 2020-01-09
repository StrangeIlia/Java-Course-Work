package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.Playlist;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.entity.dto.*;
import bgty.vt_41.bi.service.UserService;
import bgty.vt_41.bi.util.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping(value = "/create")
    public ResponseEntity<OperationResult> create(@RequestBody CreationUserForm creationUserForm) {
        if (!creationUserForm.isValid())
            return ResponseEntity.ok(new ORReject("Не верная форма данных"));
        try {
            User savedUser = userService.create(
                    creationUserForm.getUsername(),
                    creationUserForm.getPassword(),
                    creationUserForm.getEmail()
            );
            if (savedUser == null)
                return ResponseEntity.ok(new ORReject());
            else
                return ResponseEntity.ok(new AuthUserResult(savedUser.getAccessToken()));
        } catch (UserException e) {
            return ResponseEntity.ok(new ORReject(e.getMessage()));
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<OperationResult> update(@RequestBody UpdateUserForm updateUserForm,
                                                  Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        try {
            userService.update(
                    user,
                    updateUserForm.getOldPassword(),
                    updateUserForm.getUsername(),
                    updateUserForm.getNewPassword(),
                    updateUserForm.getEmail()
            );
            return new ResponseEntity<>(new ORSuccess(), HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(new ORReject(e.getMessage()), HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete")
    public void deleteUser(Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
        userService.delete(user);
    }

    @GetMapping("/loaded_video")
    public Collection<Video> getLoadedVideo(@RequestParam String username) {
        return userService.getLoadedVideo(username);
    }

    @GetMapping("/favorite_video")
    public Collection<Video> getFavoriteVideo(@RequestParam String username) {
        return userService.getFavoriteVideo(username);
    }

    @GetMapping("/playlists")
    public Collection<Playlist> getCreatedPlaylists(@RequestParam String username) {
        return userService.getCreatedPlaylists(username);
    }
}
