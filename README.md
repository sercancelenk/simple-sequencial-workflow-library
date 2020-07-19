**Simple Sequencial Workflow Engine in Java**

**Features**
- This library is written for managing sequencial flow steps for your own business.
- It also supports both of future and reactive.
- AbstractContext object will be share for steps. If you would like to put any object to it, you can do and use in step.

**Usage**

```
    @Bean
    public WorkFlow registerWorkflow(){
        Step step1 = new CreateUserStep();
        Step step2 = new SendMailStep();

        return new WorkFlow("Register Flow", Arrays.asList(step1, step2));
    }

```

```
    @Autowired WorkFlow registerWorkFlow;

    - Mono version
    Context context = new ParameterContext(Map.of("parameter1", "parameter1 value"));
    registerWorkflow.processWorkflow(context).subscribe();

    - CompletableFuture version
    Context context = new ParameterContext(Map.of("parameter1", "parameter1 value"));
    registerWorkflow.processWorkflowFuture(context).join();

    - Blocking version
    Context context = new ParameterContext(Map.of("parameter1", "parameter1 value"));
    registerWorkflow.processWorkflow(context);
    
        
```

