package bgty.vt_41.bi.entity.dto;

import lombok.Data;

@Data
public class CreationUserForm {
    private String username, password, email;

    public boolean isValid()
    {
        return getUsername() != null && getEmail() != null && getPassword() != null;
    }
}
