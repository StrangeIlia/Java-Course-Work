package bgty.vt_41.bi.entity.dto;

import bgty.vt_41.bi.entity.enums.Status;

public class ORSuccess extends OperationResult {
    public ORSuccess() {
        super();
        setStatus(Status.SUCCESS);
    }
}
