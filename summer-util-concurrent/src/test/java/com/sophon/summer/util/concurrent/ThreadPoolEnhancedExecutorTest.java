
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
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(10);
//        int corePoolSize = Runtime.getRuntime().availableProcessors();
        int corePoolSize = 4;
        int maximumPoolSize = corePoolSize * 2;
        executor = new ThreadPoolEnhancedExecutor(corePoolSize + 1, maximumPoolSize + 1, 3,
                TimeUnit.MINUTES, workQueue, new ThreadPoolEnhancedExecutor.DiscardPolicy());

    }

    @Test(expected = NullPointerException.class)
    public void TestExecuteNull() {
        executor.execute(null);
    }

    @Test
    public void TestExecuteCorePoolSizeLimitation(){
        int count = 5;
        for (int i = 0; i < count; i++) {
            executor.execute(() -> System.out.println(Thread.currentThread().getName()));
        }
        // The number must be 5
        Assert.assertEquals(5, executor.getPoolSize());

        while (executor.getIdleWorkerCount().get() <= 0) {
        }
        executor.execute(() -> System.out.println(Thread.currentThread().getName()));
        Assert.assertEquals(5, executor.getPoolSize());
    }

    @Test
    public void TestExecuteMaxPoolSizeLimitation() {
        int count = 10;
        final CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            executor.execute(() -> {
                try {
                    latch.await();
                    System.out.println(Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            latch.countDown();
        }
        Assert.assertEquals(9, executor.getPoolSize());
    }

    @Test
    public void TestExecuteShutDown() throws Exception {
        new Thread(() -> executor.shutdown()).start();
        Thread.sleep(100);
        executor.execute(() -> System.out.println(Thread.currentThread().getName()));
    }

}
