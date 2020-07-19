package byzas.libs.workflow.engine.handler;

/**
 * @author ext0280263 on 19.07.2020
 * @project IntelliJ IDEA
 */
public interface Context {
    Object getParametersAttribute(String name);

    void setParametersAttribute(String name, Object value);

    Object getLoggingParametersAttribute(String name);

    void setLoggingParametersAttribute(String name, Object value);

    void dumpLogs();

    void dumpContextParameters();
}
