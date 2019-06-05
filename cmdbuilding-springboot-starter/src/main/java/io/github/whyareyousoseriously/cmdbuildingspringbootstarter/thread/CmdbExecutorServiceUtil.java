package io.github.whyareyousoseriously.cmdbuildingspringbootstarter.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenzhen
 * Created by chenzhen on 2019/5/15.
 */
public class CmdbExecutorServiceUtil {
    private static final int CORE_POOL_SIZE = 50;
    private static final int MAXNUM_POOL_SIZE = 200;
    private static final long KEEP_ALIVE_TIME = 60L;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    /**
     * 线程工厂
     */
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = this.defaultFactory.newThread(r);
            if (!thread.isDaemon()){
                thread.setDaemon(true);
            }
            thread.setName("thread-"+this.threadNumber.getAndIncrement());
            return thread;
        }
    };

    /**
     * 生成executorService
     * @return executorService
     */
    public static ExecutorService newExecutorService(){
        //采用有界队列，大小为200000
        return new ThreadPoolExecutor(CORE_POOL_SIZE,MAXNUM_POOL_SIZE,KEEP_ALIVE_TIME,TIME_UNIT,new ArrayBlockingQueue<>(200000),THREAD_FACTORY);
    }


}
