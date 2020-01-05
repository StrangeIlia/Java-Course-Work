package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.dto.*;
import bgty.vt_41.bi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/site")
public class SiteController {
    @Autowired
    AuthService authService;

    public ResponseEntity<OperationResult> login(@ModelAttribute LoginForm loginForm)
    {
        String token = authService.login(loginForm.getUsername(), loginForm.getPassword());
        if(!token.isEmpty())
            return ResponseEntity.ok(new AuthUserResult(token));
        else
            return ResponseEntity.ok(new ORReject("Неверный логин или пароль"));
    }

    @PostMapping(value = "/login", consumes = "multipart/form-data")
    public ResponseEntity<OperationResult> loginV2(@ModelAttribute LoginForm loginForm)
    {
        return login(loginForm);
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<OperationResult> loginV1(@RequestBody LoginForm loginForm)
    {
        return login(loginForm);
    }

    @GetMapping(value = "/get_username", produces = "application/json")
    public Object getUsername(Authentication authentication)
    {
        if(authentication == null) return null;
        User user = (User) authentication.getPrincipal();
        return new Object(){
            public String username = user.getUsername();
        };
    }

    @PostMapping("/logout")
    public ResponseEntity<OperationResult> logout(Authentication authentication)
    {
        if(authentication == null) return ResponseEntity.ok(new ORReject());
        if(authService.logout((User)authentication.getPrincipal()))
            return ResponseEntity.ok(new ORSuccess());
        else
            return ResponseEntity.ok(new ORReject());
    }
}
