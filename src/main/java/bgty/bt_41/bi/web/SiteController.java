package bgty.bt_41.bi.web;

import bgty.bt_41.bi.entity.domain.User;
import bgty.bt_41.bi.entity.dto.AuthUserResult;
import bgty.bt_41.bi.entity.dto.ORReject;
import bgty.bt_41.bi.entity.dto.OperationResult;
import bgty.bt_41.bi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/site")
public class SiteController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    public OperationResult login(@RequestParam String username, @RequestParam String password)
    {
        Optional<User> optionalUser = userRepository.login(username, password);
        if(optionalUser.isPresent())
            return new AuthUserResult(optionalUser.get().getAccessToken());
        else
            return new ORReject("Неверный логин или пароль");
    }

    @GetMapping("/get_username")
    public OperationResult getUsername()
    {
        return new ORReject("Пока не реализовано");
    }

    @PostMapping("/logout")
    public OperationResult logout()
    {
        return new ORReject("Пока не реализовано");
    }
}
