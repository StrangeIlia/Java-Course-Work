package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.dto.*;
import bgty.vt_41.bi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/site")
public class SiteController {
    @Autowired
    AuthService authService;

    @PostMapping(value = "/login", consumes = "application/json")
    public OperationResult loginV1(@RequestBody LoginForm loginForm)
    {
        String token = authService.login(loginForm.getUsername(), loginForm.getPassword());
        if(!token.isEmpty())
            return new AuthUserResult(token);
        else
            return new ORReject("Неверный логин или пароль");
    }


    @GetMapping("/get_username")
    public String getUsername(Authentication authentication)
    {
        User user = (User)authentication.getPrincipal();
        return user.getUsername();
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
