package byzas.libs.workflow.engine.handler;

import byzas.libs.workflow.engine.exception.WorkflowException;
import byzas.libs.workflow.engine.util.LogUtil;
import lombok.Getter;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

/**
 * @author ext0280263 on 19.07.2020
 * @project IntelliJ IDEA
 */

@Getter
abstract class Workflow implements WorkflowBase {
    protected final List<Step> steps;
    protected final String workFlowName;
    protected LogUtil logUtil;
    protected boolean disableLogging = false;

    public Workflow(String workFlowName, List<Step> steps) {
        this.steps = steps;
        this.workFlowName = workFlowName;
        this.logUtil = new LogUtil(disableLogging);
    }

    public Workflow(String workFlowName, List<Step> steps, boolean disableLogging) {
        this.steps = steps;
        this.workFlowName = workFlowName;
        this.disableLogging = disableLogging;
        this.logUtil = new LogUtil(disableLogging);
    }

    @Override
    public CompletableFuture<Boolean> processWorkflowFuture(Context context) {
        List<Step> steps = getWorkflowSteps();
        logUtil.info("[WORKFLOW] [{}] Started.", getWorkFlowName());
        logUtil.info("[{}] Steps", getWorkFlowName());
        steps.stream().forEach(action -> logUtil.info("- {}", action.getName()));
        return steps.stream()
                .reduce(CompletableFuture.completedFuture(true),
                        (f, method) -> f.thenComposeAsync(result -> {
                            if (result) {
                                return method.doActionFuture(context)
                                        .handle((r, t) -> {
                                            if (Optional.ofNullable(t).isPresent()) {
                                                logUtil.info("[WORKFLOW-STEP] [{}] completed exceptionally.", method.getName());
                                                CompletableFuture<Boolean> rr = new CompletableFuture<>();
                                                rr.completeExceptionally(new RuntimeException(t));
                                                return rr;
                                            }
                                            logUtil.info("[WORKFLOW-STEP] [{}] completed successfully.", method.getName());
                                            return CompletableFuture.completedFuture(r);
                                        })
                                        .thenComposeAsync(Function.identity());
                            }
                            return CompletableFuture.completedFuture(false);
                        }),
                        (f1, f2) -> f1.thenCombine(f2, (result1, result2) -> result1 && result2))
                .handle((r, ex) -> {
                    if (ex instanceof CompletionException || ex instanceof RuntimeException) {
                        logUtil.error("[WORKFLOW] [{}] completed exceptionally.", ex, getWorkFlowName());
                    } else {
                        logUtil.info("[WORKFLOW] [{}] completed successfully.", getWorkFlowName());
                    }

                    return r;
                });
    }

    @Override
    public Mono<Boolean> processWorkflowMono(Context context) {
        List<Step> steps = getWorkflowSteps();
        logUtil.info("[WORKFLOW] [{}] Started.", getWorkFlowName());
        logUtil.info("[{}] Steps", getWorkFlowName());
        steps.stream().forEach(action -> logUtil.info("- {}", action.getName()));
        logUtil.info("----------------------------------------------------");
        return steps.stream()
                .reduce(Mono.just(true),
                        (f, method) -> f.flatMap(result -> {
                            if (result) {
                                logUtil.info("[WORKFLOW-STEP] [{}] started.", method.getName());
                                return method.doActionMono(context)
                                        .onErrorResume(t -> {
                                            logUtil.info("[WORKFLOW-STEP] [{}] completed exceptionally.", method.getName());
                                            return Mono.error(new WorkflowException(t));
                                        })
                                        .doOnSuccess(d -> {
                                            logUtil.info("[WORKFLOW-STEP] [{}] completed successfully.", method.getName());
                                        });

                            } else {
                                return Mono.just(false);
                            }
                        }),
                        (f1, f2) -> f1.zipWith(f2, (result1, result2) -> result1 && result2))
                .doOnError(t -> {
                    logUtil.error("[WORKFLOW] [{}] completed exceptionally.", t.getCause(), getWorkFlowName());
                })
                .doOnSuccess(d -> {
                    logUtil.info("[WORKFLOW] [{}] completed successfully.", getWorkFlowName());
                });
    }

    @Override
    public Boolean processWorkflow(Context context) {
        List<Step> steps = getWorkflowSteps();
        logUtil.info("[WORKFLOW] [{}] Started.", getWorkFlowName());
        logUtil.info("[{}] Steps", getWorkFlowName());
        steps.stream().forEach(action -> logUtil.info("- {}", action.getName()));
        logUtil.info("----------------------------------------------------");

        try {
            boolean endResult = steps.stream()
                    .reduce(true,
                            (acc, step) -> {
                                if (acc) {
                                    try {
                                        logUtil.info("[WORKFLOW-STEP] [{}] started.", step.getName());
                                        boolean stepResult = step.doAction(context);
                                        logUtil.info("[WORKFLOW-STEP] [{}] completed successfully.", step.getName());
                                        return stepResult;
                                    } catch (Exception e) {
                                        logUtil.info("[WORKFLOW-STEP] [{}] completed exceptionally.", step.getName());
                                        throw new WorkflowException(e);
                                    }
                                }
                                return false;
                            },
                            (f1, f2) -> f1 && f2);

            if (endResult) logUtil.info("[WORKFLOW] [{}] completed successfully.", getWorkFlowName());

            return endResult;
        } catch (Exception e) {
            logUtil.error("[WORKFLOW] [{}] completed exceptionally.", e, getWorkFlowName());
            return false;
        }
    }

    private List<Step> getWorkflowSteps() {
        List<Step> steps = getSteps();
        if (CollectionUtils.isEmpty(steps)) {
            logUtil.error("There is no defined step for " + getWorkFlowName());
            throw new IllegalArgumentException("There is no defined step for " + getWorkFlowName());
        }
        return steps;
    }


}