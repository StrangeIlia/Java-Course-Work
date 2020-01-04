package bgty.bt_41.bi.service.imp;

import bgty.bt_41.bi.entity.domain.User;
import bgty.bt_41.bi.repository.UserRepository;
import bgty.bt_41.bi.service.AuthService;
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
        Optional<User> optionalUser = userRepository.login(username,password);
        if(optionalUser.isPresent()){
            String token = UUID.randomUUID().toString();
            User user = optionalUser.get();
            user.setAccessToken(token);
            userRepository.save(user);
            return token;
        }

        return StringUtils.EMPTY;
    }

    @Override
    public Optional<User> findByToken(String token) {
        return userRepository.findByAccessToken(token);
    }
}