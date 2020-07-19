package byzas.libs.workflow.engine.handler;

import lombok.ToString;

import java.util.Map;

/**
 * @author ext0280263 on 19.07.2020
 * @project IntelliJ IDEA
 */

@ToString
public class ParameterContext extends AbstractContext {
    public ParameterContext() {}

    public ParameterContext(Map<String, Object> parameters) {
        super(parameters);
    }
}