package com.framgia.rssfeed.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yue on 09/05/2016.
 */
public class MonitorWorkerThreadUtil {

    private final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private final int KEEP_ALIVE_TIME = 1;
    private final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private ThreadPoolExecutor mThreadPool;
    private static MonitorWorkerThreadUtil sInstance;

    private MonitorWorkerThreadUtil() {
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque<>();
        mThreadPool = new ThreadPoolExecutor(NUMBER_OF_CORES
                , NUMBER_OF_CORES
                , KEEP_ALIVE_TIME
                , KEEP_ALIVE_TIME_UNIT
                , blockingQueue);
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
        mThreadPool.execute(worker);
    }
}
