
package com.sophon.summer.util.concurrent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Andy Ji jifengnan@126.com
 */
public class ThreadPoolEnhancedExecutorTest {
    private ThreadPoolEnhancedExecutor executor;

    @Before
    public void prepareExecutor() {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(100);
//        int corePoolSize = Runtime.getRuntime().availableProcessors();
        int corePoolSize = 4;
        int maximumPoolSize = corePoolSize * 2;
        executor = new ThreadPoolEnhancedExecutor(corePoolSize + 1, maximumPoolSize + 1, 3,
                TimeUnit.MINUTES, workQueue);

    }

    /**
     * Proceed in 4 steps:
     * <p>
     * 1. If fewer than corePoolSize threads are running, try to
     * start a new thread with the given command as its first
     * task.  The call to addWorker atomically checks runState and
     * workerCount, and so prevents false alarms that would add
     * threads when it shouldn't, by returning false.
     * <p>
     * 2. If there's no any idle thread and fewer than maximumPoolSize
     * threads are running, try to start a new thread with the given
     * command as its first task.
     * <p>
     * 3. If a task can be successfully queued, then we still need
     * to double-check whether we should have added a thread
     * (because existing ones died since last checking) or that
     * the pool shut down since entry into this method. So we
     * recheck state and if necessary roll back the enqueuing if
     * stopped, or start a new thread if there are none.
     * <p>
     * 4. If we cannot queue task, then we try to add a new
     * thread.  If it fails, we know we are shut down or saturated
     * and so reject the task.
     */
    @Test(expected = NullPointerException.class)
    public void TestExecuteNull() {
        executor.execute(null);
    }

    @Test
    public void TestMaxCoreLimitation() throws Exception{
        int count = 10;
        final CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            executor.execute(() -> {
                try {
                    latch.await();
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            latch.countDown();
        }
        Assert.assertEquals(9, executor.getPoolSize());
        Thread.sleep(2000);
    }


}
