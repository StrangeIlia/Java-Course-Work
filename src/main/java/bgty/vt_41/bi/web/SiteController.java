package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.dto.AuthUserResult;
import bgty.vt_41.bi.entity.dto.ORReject;
import bgty.vt_41.bi.entity.dto.ORSuccess;
import bgty.vt_41.bi.entity.dto.OperationResult;
import bgty.vt_41.bi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/site")
public class SiteController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public OperationResult login(@RequestParam String username, @RequestParam String password)
    {
        String token = authService.login(username, password);
        if(!token.isEmpty())
            return new AuthUserResult(token);
        else
            return new ORReject("Неверный логин или пароль");
    }

    @GetMapping("/get_username")
    public String getUsername(Authentication authentication)
    {
        return ((User)authentication.getPrincipal()).getUsername();
    }

    @PostMapping("/logout")
    public OperationResult logout(Authentication authentication)
    {
        if(authService.logout((User)authentication.getPrincipal()))
            return new ORSuccess();
        else
            return new ORReject();
    }
}
