package bgty.vt_41.bi.web.exceptions;

import org.springframework.security.core.AuthenticationException;

public class DefaultAuthenticationException extends AuthenticationException {
    public DefaultAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }
}
