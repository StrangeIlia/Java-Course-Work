package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.dto.AuthUserResult;
import bgty.vt_41.bi.entity.dto.ORReject;
import bgty.vt_41.bi.entity.dto.ORSuccess;
import bgty.vt_41.bi.entity.dto.OperationResult;
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

    @PostMapping(value = "/login")
    public ResponseEntity<OperationResult> login(@RequestParam String username, @RequestParam String password)
    {
        String token = authService.login(username, password);
        if(!token.isEmpty())
            return new ResponseEntity<>(new AuthUserResult(token), HttpStatus.OK);
        else
            return new ResponseEntity<>(new ORReject("Неверный логин или пароль"), HttpStatus.OK);
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
