package bgty.vt_41.bi.service.imp;

import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.repository.UserRepository;
import bgty.vt_41.bi.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.UUID;



@Service
public class DefaultAuthService implements AuthService {

    @Autowired
    UserRepository userRepository;

    @Override
    public String login(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.checkPassword(password))
            {
                String token = UUID.randomUUID().toString();
                user.setAccessToken(token);
                user = userRepository.save(user);
                if(user != null)
                    return token;
            }
        }
        return StringUtils.EMPTY;
    }

    @Override
    public boolean logout(User user) {
        if(user == null) return false;
        String token = UUID.randomUUID().toString();
        user.setAccessToken(token);
        return userRepository.save(user) != null;
    }

    @Override
    public Optional<User> findByToken(String token) {
        return userRepository.findByAccessToken(token);
    }
}