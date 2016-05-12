package com.framgia.rssfeed.util;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by yue on 09/05/2016.
 */
public class MonitorWorkerThreadUtil {

    private static MonitorWorkerThreadUtil sInstance;
    private final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private final int KEEP_ALIVE_TIME = 1;
    private final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private PriorityThreadPoolExecutor mPriorityThreadPool;

    private MonitorWorkerThreadUtil() {
        mPriorityThreadPool = new PriorityThreadPoolExecutor(NUMBER_OF_CORES
                , NUMBER_OF_CORES
                , KEEP_ALIVE_TIME
                , KEEP_ALIVE_TIME_UNIT
                , new PriorityBlockingQueue<Runnable>());
    }

    public static MonitorWorkerThreadUtil getInstance() {
        if (sInstance == null) {
            synchronized (MonitorWorkerThreadUtil.class) {
                if (sInstance == null) {
                    sInstance = new MonitorWorkerThreadUtil();
                }
            }
        }
        return sInstance;
    }

    public void assign(WorkerThread worker) {
        final RunnableFuture<Object> futureTask = mPriorityThreadPool.newTaskForValue(worker, null);
        mPriorityThreadPool.execute(futureTask);
    }

    public void clear() {
        mPriorityThreadPool.getQueue().clear();
    }
}
