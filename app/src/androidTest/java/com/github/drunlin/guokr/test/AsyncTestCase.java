package com.github.drunlin.guokr.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 提供测试异步任务的简单方法。
 *
 * @author drunlin@outlook.com
 */
public abstract class AsyncTestCase extends TestCase {
    private static final int TIMEOUT = 5_000;

    private CountDownLatch signal;

    private int downedCount;

    protected void await(int count, long timeOut) {
        signal = new CountDownLatch(count);
        try {
            signal.await(timeOut, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void await(int count) {
        await(count - downedCount, Long.MAX_VALUE);

        downedCount = 0;
    }

    protected void await() {
        await(1, TIMEOUT);
    }

    protected void countDown() {
        if (signal != null) {
            signal.countDown();
        } else {
            downedCount += 1;
        }
    }
}
