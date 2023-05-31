package org.js.fileRpc.user.provider.thread;

import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 多任务调度器，用于多线程执行任务
 **/
@Component
public final class MultiTaskScheduler {
    private static final Logger logger = LoggerFactory.getLogger(MultiTaskScheduler.class);


    private static  DtpExecutor myDtpExecutor = DtpRegistry.getDtpExecutor("myDtpExecutor");

    private List<Runnable> list = new ArrayList<>();

    public MultiTaskScheduler() {
    }

    public void add(Runnable... runners) {
        if (runners != null && runners.length != 0) {
            list.addAll(Arrays.asList(runners));
        } else {
            throw new IllegalArgumentException("runners must not be null nor empty");
        }
    }

    public void start(boolean await) {
        start(await, 10000);
    }

    public void start(boolean await, final int timeout) {
        CountDownLatch countDownLatch = submitTask(list);
        if (await) {
            try {
                countDownLatch.await(timeout, TimeUnit.MILLISECONDS);  //阻塞等待, 直到所有任务结束
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public void shutdown() {
        // execService.shutdown();
        list.clear();
    }

    /**
     * 提交任务，并等待执行结束
     **/
    public static void await(long timeout, TimeUnit timeUnit, List<Runnable> threads) {
        CountDownLatch countDownLatch = submitTask(threads);
        try {
            countDownLatch.await(timeout, timeUnit);  //阻塞等待, 直到所有任务结束
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 提交任务，并等待执行结束
     **/
    public static void await(long timeout, TimeUnit timeUnit, Runnable... runnable) {
        List<Runnable> threads = Arrays.asList(runnable);
        await(timeout, timeUnit, threads);
    }

    /**
     * 提交任务，并等待执行结束
     **/
    public static void await(List<Runnable> threads) {
        CountDownLatch countDownLatch = submitTask(threads);
        try {
            countDownLatch.await(); //阻塞等待, 直到所有任务结束
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 提交任务，并等待执行结束
     **/
    public static void await(Runnable... runnable) {
        List<Runnable> threads = Arrays.asList(runnable);
        await(threads);
    }

    /**
     * 提交任务
     **/
    private static CountDownLatch submitTask(List<Runnable> threads) {
        Assert.notNull(threads, "threads must not be null");
        logger.info("submit {} tasks, corePoolSize: {}, maximumPoolSize: {}, queueCapacity: {}, poolSize: {}, activeCount: {}, queueSize: {}",
                threads.size(), myDtpExecutor.getCorePoolSize(), myDtpExecutor.getMaximumPoolSize(), myDtpExecutor.getPoolSize(), myDtpExecutor.getActiveCount(), myDtpExecutor.getQueue().size());
        //String traceId = RequestTraceIdHolder.getTraceId();
        Map<String, String> dcMap = MDC.getCopyOfContextMap();
        CountDownLatch countDownLatch = new CountDownLatch(threads.size());
        threads.forEach(thread -> myDtpExecutor.submit(() -> {
            long st = System.currentTimeMillis();
            //RequestTraceIdHolder.setTraceId(traceId);
            if (dcMap != null) {
                MDC.setContextMap(dcMap);
            }
            try {
                thread.run();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                logger.info("cost {} ms, corePoolSize: {}, maximumPoolSize: {}, queueCapacity: {}, poolSize: {}, activeCount: {}, queueSize: {}",
                        System.currentTimeMillis() - st, threads.size(), myDtpExecutor.getCorePoolSize(), myDtpExecutor.getMaximumPoolSize(), myDtpExecutor.getPoolSize(), myDtpExecutor.getActiveCount(), myDtpExecutor.getQueue().size());
                countDownLatch.countDown();
            }
        }));
        return countDownLatch;
    }
}


