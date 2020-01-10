package bgty.vt_41.bi.util.exceptions;

public class PlaylistException extends Exception {
    public PlaylistException() {
        super("Неизвестная ошибка");
    }

    public PlaylistException(String message) {
        super(message);
    }
}
