package com.vdq.autogpm.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.*;
public class ProfileExecutor {

    private static final int NUM_THREADS = 5;
    private final ExecutorService executorService;

    public ProfileExecutor() {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        executorService = new ThreadPoolExecutor(NUM_THREADS, NUM_THREADS,
                0L, TimeUnit.MILLISECONDS, queue);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void shutdown() {
        executorService.shutdown();
    }



}
