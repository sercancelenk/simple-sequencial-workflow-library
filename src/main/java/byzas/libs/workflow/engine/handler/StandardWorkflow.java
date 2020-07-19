package byzas.libs.workflow.engine.handler;

import java.util.List;

/**
 * @author ext0280263 on 19.07.2020
 * @project IntelliJ IDEA
 */

public class StandardWorkflow extends Workflow {
    public StandardWorkflow(String workFlowName, List<Step> steps) {
        super(workFlowName, steps);
    }

    public StandardWorkflow(String workFlowName, List<Step> steps, boolean disableLogging) {
        super(workFlowName, steps, disableLogging);
    }
}
