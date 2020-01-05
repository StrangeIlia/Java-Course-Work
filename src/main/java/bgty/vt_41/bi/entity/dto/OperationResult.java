package bgty.vt_41.bi.entity.dto;

import bgty.vt_41.bi.entity.enums.EStatus;
import bgty.vt_41.bi.util.json_serializer.ORSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class OperationResult {
    @JsonSerialize(using = ORSerializer.class)
    private EStatus status;
}
