package bgty.vt_41.bi.util.exceptions;

public class UserException extends Exception {
    public UserException() {
        super("Неизвестная ошибка");
    }

    public UserException(String message) {
        super(message);
    }
}
