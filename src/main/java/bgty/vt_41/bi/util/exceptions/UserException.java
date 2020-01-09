package bgty.vt_41.bi.util.exceptions;

import lombok.Getter;

@Getter
public class UserException extends Exception {
    private String message;

    public UserException() {
        this.message = "Неизвестная ошибка";
    }

    public UserException(String message) {
        this.message = message;
    }
}
