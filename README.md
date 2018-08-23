# summer-util
Provides some useful tools

## summer-util-concurrent
Provides some useful concurrent tools
### ThreadPoolEnhancedExecutor
This thread pool enhanced the `java.util.concurrent.ThreadPoolExecutor` to
 support the case: if there's no any idle thread, 
 and fewer than maximumPoolSize threads are running, 
 a new thread will be created instead of to offer queue directly.
