package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.Playlist;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.entity.dto.AuthUserResult;
import bgty.vt_41.bi.entity.dto.ORReject;
import bgty.vt_41.bi.entity.dto.ORSuccess;
import bgty.vt_41.bi.entity.dto.OperationResult;
import bgty.vt_41.bi.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<OperationResult> create(@RequestPart String username, @RequestPart String email, @RequestPart String password)
    {
        Optional<User> user = userService.findByUsername(username);
        if(user.isPresent()){
            return new ResponseEntity<>(new ORReject("Пользователь с таким логином уже существует"), HttpStatus.OK);
        }
        user = userService.findByEmail(username);
        if(user.isPresent()){
            return new ResponseEntity<>(new ORReject("Данная электронная почта уже привязана к другому аккаунту"), HttpStatus.OK);
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        User savedUser = userService.save(newUser);
        if(savedUser == null)
            return new ResponseEntity<>(new ORReject(), HttpStatus.OK);
        else
            return new ResponseEntity<>(new AuthUserResult(savedUser.getAccessToken()), HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<OperationResult> update(@RequestPart String oldPassword,
                                  @RequestPart(required = false) String username,
                                  @RequestPart(required = false) String email,
                                  @RequestPart(required = false) String newPassword,
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
    public Collection<Video> getLoadedVideo(@RequestParam(required = false) String username,
                                            HttpServletRequest request)
    {
        if(!username.equals(""))
        {
            Optional<User> optionalUser = userService.findByUsername(username);
            if(optionalUser.isEmpty()) return null;
            else return optionalUser.get().getLoadedVideo();
        }
        else{
            Optional<String> tokenParam =  Optional.ofNullable(request.getHeader(AUTHORIZATION));
            if(tokenParam.isPresent())
            {
                String token = tokenParam.get();
                token = StringUtils.removeStart(token, "Bearer").trim();
                Optional<User> optionalUser = userService.findByToken(token);
                if(optionalUser.isPresent())
                {
                    User user = optionalUser.get();
                    return user.getLoadedVideo();
                }
            }
        }
        return null;
    }

    @GetMapping("/favorite_video")
    public Collection<Video> getFavoriteVideoNonAuth(@RequestParam String username,
                                                     Authentication authentication)
    {
        if(username != null)
        {
            Optional<User> optionalUser = userService.findByUsername(username);
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
