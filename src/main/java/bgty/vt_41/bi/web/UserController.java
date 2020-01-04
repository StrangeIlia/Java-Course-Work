package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.dto.AuthUserResult;
import bgty.vt_41.bi.entity.dto.ORReject;
import bgty.vt_41.bi.entity.dto.OperationResult;
import bgty.vt_41.bi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    //@RequestMapping(name = "/create", produces = "application/json") // старая версия, оставил чтобы не
    @PostMapping("/create")
    public OperationResult create(@RequestParam String username, @RequestParam String email, @RequestParam String password)
    {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return new ORReject("Пользователь с таким логином уже существует");
        }
        user = userRepository.findByEmail(username);
        if(user.isPresent()){
            return new ORReject("Данная электронная почта уже привязана к другому аккаунту");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        User savedUser = userRepository.save(newUser);
        if(savedUser == null)
            return new ORReject();
        else
            return new AuthUserResult(savedUser.getAccessToken());
    }
}
