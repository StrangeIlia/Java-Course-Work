package bgty.bt_41.bi.entity.dto;

import lombok.Data;

@Data
public class AuthUserResult extends ORSuccess {
    private String token;

    public AuthUserResult(String token)
    {
        super();
        setToken(token);
    }
}
