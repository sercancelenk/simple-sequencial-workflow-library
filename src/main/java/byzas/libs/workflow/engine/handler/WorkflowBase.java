package byzas.libs.workflow.engine.handler;

import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

/**
 * @author ext0280263 on 19.07.2020
 * @project IntelliJ IDEA
 */

public interface WorkflowBase {
    Boolean processWorkflow(Context context);

    Mono<Boolean> processWorkflowMono(Context context);

    CompletableFuture<Boolean> processWorkflowFuture(Context context);
}
