package byzas.libs.workflow.engine.handler;

import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

/**
 * @author ext0280263 on 19.07.2020
 * @project IntelliJ IDEA
 */
public interface Step {
    String getName();

    Mono<Boolean> doActionMono(Context context);

    CompletableFuture<Boolean> doActionFuture(Context context);

    Boolean doAction(Context context);
}
