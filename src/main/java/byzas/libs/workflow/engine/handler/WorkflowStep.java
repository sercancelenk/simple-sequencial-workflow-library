package byzas.libs.workflow.engine.handler;

import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

/**
 * @author ext0280263 on 19.07.2020
 * @project IntelliJ IDEA
 */
public abstract class WorkflowStep implements Step {

    @Override
    public Mono<Boolean> doActionMono(Context context) {
        //Not implemented
        return Mono.error(new RuntimeException("Mono not implemented"));
    }

    @Override
    public CompletableFuture<Boolean> doActionFuture(Context context) {
        // Not implemented
        CompletableFuture<Boolean> result = CompletableFuture.completedFuture(false);
        result.completeExceptionally(new RuntimeException("Future Not implemented"));
        return result;
    }

    @Override
    public Boolean doAction(Context context) {
        // Not implemented
        throw new RuntimeException("Not implemented");
    }
}