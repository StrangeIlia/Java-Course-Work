package bgty.vt_41.bi.util.exceptions;

public class VideoException extends Exception {
    public VideoException() {
        super("Неизвестная ошибка");
    }

    public VideoException(String message) {
        super(message);
    }
}
