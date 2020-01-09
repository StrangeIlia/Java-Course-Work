package bgty.vt_41.bi.util.exceptions;

import lombok.Getter;

@Getter
public class VideoException extends Exception {
    private String message;

    public VideoException() {
        this.message = "Неизвестная ошибка";
    }

    public VideoException(String message) {
        this.message = message;
    }
}
