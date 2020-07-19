package byzas.libs.workflow.engine.handler;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ext0280263 on 19.07.2020
 * @project IntelliJ IDEA
 */

@Log4j2
abstract class AbstractContext implements Context {
    private final ConcurrentHashMap<String, Object> contextParameters;
    private final ConcurrentHashMap<String, Object> loggingContextParameters;

    protected AbstractContext(){
        this.contextParameters = new ConcurrentHashMap<>();
        this.loggingContextParameters = new ConcurrentHashMap<>();
    }

    protected AbstractContext(Map<String, Object> parameters) {
        this.contextParameters = new ConcurrentHashMap<>(parameters);
        this.loggingContextParameters = new ConcurrentHashMap<>();
    }

    @Override
    public Object getParametersAttribute(String name) {
        return this.contextParameters.get(name);
    }

    @Override
    public void setParametersAttribute(String name, Object value) {
        this.contextParameters.put(name, value);
    }

    @Override
    public Object getLoggingParametersAttribute(String name) {
        return this.contextParameters.get(name);
    }

    @Override
    public void setLoggingParametersAttribute(String name, Object value) {
        this.contextParameters.put(name, value);
    }

    @Override
    public void dumpLogs() {
        log.info("{}", this.loggingContextParameters);
    }

    @Override
    public void dumpContextParameters() {
        log.info("{}", this.contextParameters);
    }
}