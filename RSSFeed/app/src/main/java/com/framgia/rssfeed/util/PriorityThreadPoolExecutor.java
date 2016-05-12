package com.framgia.rssfeed.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yue on 10/05/2016.
 */
public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {
    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime
            , TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    protected <T> RunnableFuture<T> newTaskForValue(Runnable runnable, T value) {
        return new ComparableFutureTask<T>(runnable, value);
    }

    protected class ComparableFutureTask<T> extends FutureTask<T> implements Comparable<ComparableFutureTask<T>> {
        private Object mObject;

        public ComparableFutureTask(Runnable runnable, T result) {
            super(runnable, result);
            mObject = runnable;
        }

        @Override
        public int compareTo(ComparableFutureTask<T> o) {
            if (this == o) return 0;
            if (o == null) return -1;
            if (mObject != null && o.mObject != null) {
                if (mObject.getClass().equals(o.mObject.getClass())) {
                    if (mObject instanceof Comparable) {
                        return ((Comparable) mObject).compareTo(o.mObject);
                    }
                }
            }
            return 0;
        }
    }
}
