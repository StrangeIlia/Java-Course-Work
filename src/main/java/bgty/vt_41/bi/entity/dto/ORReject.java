package bgty.vt_41.bi.entity.dto;

import lombok.Data;

@Data
public class ORReject extends OperationResult {
    private String error;

    public ORReject(){
        super();
        error = "Неизвестная ошибка";
    }

    public ORReject(String string){
        super();
        error = string;
    }
}
