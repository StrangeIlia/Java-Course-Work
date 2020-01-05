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

    public ResponseEntity<OperationResult> login(LoginForm loginForm)
    {
        String token = authService.login(loginForm.getUsername(), loginForm.getPassword());
        if(!token.isEmpty())
            return ResponseEntity.ok(new AuthUserResult(token));
        else
            return ResponseEntity.ok(new ORReject("Неверный логин или пароль"));
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<OperationResult> loginV1(@RequestBody LoginForm loginForm)
    {
        return  login(loginForm);
    }

    @PostMapping(value = "/login", consumes = "multipart/form-data")
    public ResponseEntity<OperationResult> loginV2(@ModelAttribute LoginForm loginForm)
    {
        return  login(loginForm);
    }

    @GetMapping("/get_username")
    public String getUsername(Authentication authentication)
    {
        User user = (User)authentication.getPrincipal();
        return user.getUsername();
    }

    @PostMapping("/logout")
    public ResponseEntity<OperationResult> logout(Authentication authentication)
    {
        if(authService.logout((User)authentication.getPrincipal()))
            return new ResponseEntity<>(new ORSuccess(), HttpStatus.OK);
        else
            return new ResponseEntity<>(new ORReject(), HttpStatus.OK);
    }
}
