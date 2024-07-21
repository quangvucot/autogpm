package com.vdq.autogpm.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.*;
public class ProfileExecutor {

    private static final int NUM_THREADS = 5;
    private ExecutorService executorService;

    public ProfileExecutor() {
        this.executorService = createExecutorService();
    }

    private ExecutorService createExecutorService() {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        return new ThreadPoolExecutor(NUM_THREADS, NUM_THREADS,
                0L, TimeUnit.MILLISECONDS, queue);
    }

    public ExecutorService getExecutorService() {
        if (executorService.isShutdown() || executorService.isTerminated()) {
            restart();
        }
        return executorService;
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public void restart() {
        if (executorService.isShutdown() || executorService.isTerminated()) {
            executorService = createExecutorService();
        }
    }


}
