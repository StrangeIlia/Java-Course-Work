package bgty.vt_41.bi.entity.dto;

import lombok.Data;

@Data
public class UpdateUserForm {
    private String username, oldPassword, newPassword, email;
}
